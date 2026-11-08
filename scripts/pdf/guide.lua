--[[
Pandoc filter used by build_pdf.py to turn the combined guide Markdown into
a single book.

The input is a sequence of top-level divs, one per guide page:

  :::::::: {.guide-page path="drawing/renderTargets.md" depth="2"}
  # Render targets and color buffers
  ...
  ::::::::

For each page this filter
  * turns its first heading into the page heading at level `depth`, and
    shifts the other headings below it,
  * gives every heading an id that is unique in the whole book, using the
    same slugs as Zola so `@/page.md#anchor` links keep working,
  * rewrites `@/page.md#anchor` links into links inside the document, and
    links to pages that are not in the book into links to the website.
]]

local site_url = "https://guide.openrndr.org"
local static_prefix = ""

local function warn(msg)
  io.stderr:write("[guide.lua] warning: " .. msg .. "\n")
end

-- Same result as Zola's `slugify.anchors = "on"` (the `slug` crate) for
-- ASCII text: lowercase, runs of other characters become a single "-".
local function zola_slug(s)
  s = s:lower():gsub("[^%w]+", "-"):gsub("^%-+", ""):gsub("%-+$", "")
  return s
end

-- "drawing/renderTargets.md" -> "drawing-renderTargets",
-- "drawing/_index.md" -> "drawing", "_index.md" -> "introduction"
local function page_id(path)
  local id = path:gsub("%.md$", ""):gsub("/?_index$", "")
  if id == "" then id = "introduction" end
  id = id:gsub("[^%w_]", "-")
  return id
end

-- Website URL of a page, e.g. "drawing/_index.md" -> ".../drawing/"
local function page_url(path, anchor)
  local p = path:gsub("%.md$", ""):gsub("_index$", "")
  if p ~= "" and not p:match("/$") then p = p .. "/" end
  local url = site_url .. "/" .. p
  if anchor and anchor ~= "" then url = url .. "#" .. anchor end
  return url
end

local pages = {} -- path -> { id = page heading id, anchors = { slug -> id } }

local function prepare_page(div)
  local path = div.attributes.path
  local depth = tonumber(div.attributes.depth) or 1
  local pid = page_id(path)
  local info = { id = pid, anchors = {} }
  pages[path] = info

  local seen = {}
  local first = true
  return div.content:walk({
    Header = function(h)
      local slug = zola_slug(pandoc.utils.stringify(h.content))
      if slug == "" then slug = "section" end
      if seen[slug] then
        local n = seen[slug]
        seen[slug] = n + 1
        slug = slug .. "-" .. n
      else
        seen[slug] = 1
      end

      if first then
        first = false
        h.level = depth
        h.identifier = pid
      else
        h.level = math.min(6, math.max(h.level + depth - 1, depth + 1))
        h.identifier = pid .. "--" .. slug
      end
      info.anchors[slug] = h.identifier
      return h
    end,
  })
end

local function resolve_link(link)
  local target = link.target
  local path, anchor = target:match("^@/([^#]*)#?(.*)$")
  if path then
    local info = pages[path]
    if info and anchor == "" then
      link.target = "#" .. info.id
      return link
    elseif info and info.anchors[anchor] then
      link.target = "#" .. info.anchors[anchor]
      return link
    end
    warn("link target not found in the book: " .. target .. ", linking to the website")
    link.target = page_url(path, anchor)
  elseif target:match("^/") then
    link.target = site_url .. target
  end
  return link
end

local function resolve_image(img)
  if img.src:match("^/") then img.src = static_prefix .. img.src end
  return img
end

local function report_raw(raw)
  if raw.format == "html" then
    warn("raw HTML is dropped from the PDF: " .. raw.text:sub(1, 60))
  end
end

function Pandoc(doc)
  if doc.meta["site-url"] then site_url = pandoc.utils.stringify(doc.meta["site-url"]) end
  if doc.meta["static-prefix"] then static_prefix = pandoc.utils.stringify(doc.meta["static-prefix"]) end

  -- Pass 1: unwrap the page divs, collecting heading ids.
  local blocks = pandoc.Blocks({})
  for _, block in ipairs(doc.blocks) do
    if block.t == "Div" and block.classes:includes("guide-page") then
      blocks:extend(prepare_page(block))
    else
      blocks:insert(block)
    end
  end

  -- Pass 2: now that all ids are known, resolve links.
  blocks = blocks:walk({
    Link = resolve_link,
    Image = resolve_image,
    RawBlock = report_raw,
    RawInline = report_raw,
  })

  return pandoc.Pandoc(blocks, doc.meta)
end
