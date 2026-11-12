// Pandoc template for the OPENRNDR guide PDF. Used by build_pdf.py.
// Pandoc fills in its template variables (dollar signs); the rest is Typst.

#let accent = rgb("#c4245b")
#let muted = luma(110)
#let main-font = ("$mainfont$", "Libertinus Serif")
#let sans-font = ("$sansfont$", "Libertinus Serif")
#let mono-font = ("$monofont$", "DejaVu Sans Mono")

// Used by pandoc for horizontal rules.
#let horizontalrule = align(center, line(length: 30%, stroke: 0.5pt + luma(180)))

$if(highlighting-definitions)$
$highlighting-definitions$
$endif$

// Media helpers, called from the Markdown that build_pdf.py generates.
#let guide-image(path, width: 85%) = figure(image(path, width: width))

#let guide-video(path, url, width: 85%) = figure(
  link(url, box(width: width, {
    image(path, width: 100%)
    place(center + horizon, circle(
      radius: 16pt,
      fill: black.transparentize(45%),
      stroke: 1.5pt + white,
      align(center + horizon, move(dx: 2pt, rotate(90deg,
        polygon.regular(vertices: 3, size: 14pt, fill: white)))),
    ))
  })),
  caption: [Video: #link(url)[watch it online]],
)

#set document(title: [$title$])
#set text(font: main-font, size: 10.5pt, lang: "en")
#set par(justify: true, leading: 0.62em, spacing: 1.1em)
#set page(paper: "a4", margin: (x: 2.3cm, top: 2.6cm, bottom: 2.4cm))

// Links: internal links in accent color, external ones also underlined.
#show link: it => {
  if type(it.dest) == str { text(fill: accent, underline(offset: 2pt, stroke: 0.4pt, it)) }
  else { text(fill: accent, it) }
}

// Code
#show raw: set text(font: mono-font)
#show raw.where(block: true): set text(size: 8.3pt)
#show raw.where(block: true): set par(justify: false)
#show raw.where(block: true): block.with(
  width: 100%, inset: (x: 9pt, y: 8pt), radius: 3pt,
  fill: luma(247), stroke: 0.5pt + luma(225),
)
// highlight, unlike box, lets long inline code wrap across lines. A box
// would push it to the next line whole, leaving a badly stretched line.
#show raw.where(block: false): highlight.with(
  fill: luma(242), extent: 1.5pt, radius: 2pt,
  top-edge: "ascender", bottom-edge: "descender",
)

// Figures: no "Figure N" numbering, small captions.
#set figure(numbering: none, gap: 0.6em)
#show figure: set block(above: 1.4em, below: 1.4em)
#show figure.caption: set text(size: 8.5pt, fill: muted)

// Tables
#set table(stroke: (x, y) => (bottom: if y == 0 { 0.8pt } else { 0.3pt + luma(200) }),
           inset: 6pt)
#show table.cell.where(y: 0): strong

#show table: set par(justify: false)

// Headings: chapters (level 1) and guide pages (level 2) are numbered.
#set heading(numbering: "1.1")
#show selector.or(..range(3, 7).map(l => heading.where(level: l))): set heading(numbering: none)
#show heading: set text(font: sans-font)
#show heading: set block(above: 1.8em, below: 0.9em)
// Only real chapters, not the title of the table of contents.
#show heading.where(level: 1, outlined: true): it => {
  pagebreak(weak: true)
  v(3cm)
  if it.numbering != none {
    text(size: 13pt, fill: accent, weight: "bold", tracking: 0.08em)[
      CHAPTER #counter(heading).display("1")
    ]
    v(0.4em)
  }
  block(below: 2.2em, text(size: 26pt, weight: "bold", it.body))
}
#show heading.where(level: 2): set text(size: 17pt)
#show heading.where(level: 2): set block(above: 2.6em, below: 1em)
#show heading.where(level: 3): set text(size: 13pt)
#show heading.where(level: 4): set text(size: 11pt)

// Table of contents
#show outline.entry.where(level: 1): set block(above: 1.2em)
#show outline.entry.where(level: 1): strong

// ---------------------------------------------------------------------------
// Cover

#page(margin: (x: 2.3cm, y: 3cm), {
  set text(font: sans-font)
  v(2cm)
  text(size: 40pt, weight: "bold", tracking: 0.04em)[$title$]
  $if(subtitle)$
  v(0.2em)
  text(size: 15pt, fill: muted)[$subtitle$]
  $endif$
  v(1.5cm)
  $if(cover-image)$
  image("$cover-image$", width: 100%)
  $endif$
  v(1fr)
  text(size: 10pt, fill: muted)[
    $if(site-url)$#link("$site-url$") \ $endif$
    $date$
  ]
})

#outline(title: [Contents], depth: 2, indent: auto)

// ---------------------------------------------------------------------------
// Body: running header with the current chapter, page numbers in the footer.

#set page(
  numbering: "1",
  header: context {
    let here-page = here().page()
    let starts = query(heading.where(level: 1)).filter(h => h.location().page() == here-page)
    let before = query(heading.where(level: 1).before(here()))
    // No header on pages where a chapter starts.
    if starts.len() == 0 and before.len() > 0 {
      set text(font: sans-font, size: 8.5pt, fill: muted)
      before.last().body
      h(1fr)
      [$title$]
      v(-0.5em)
      line(length: 100%, stroke: 0.4pt + luma(200))
    }
  },
)
#counter(page).update(1)

$body$
