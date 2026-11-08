/*
 * Tables: wrapped in a div.table-wrapper like just-the-docs does, which
 * styles them and makes wide tables scroll horizontally on small screens.
 *
 * Code blocks:
 * - Kotlin programs include their import statements so they can be copied
 *   and run. The imports are hidden on the page to keep examples short.
 * - Every code block gets a copy button which copies the full code,
 *   including the hidden imports. The full code is also kept in the
 *   code element's data-raw-code attribute.
 */
(function () {
  "use strict";

  function lineText(line) {
    return line.textContent.trim();
  }

  // Removes a line <span> and the "\n" text node that follows it.
  function removeLine(line) {
    var next = line.nextSibling;
    if (next && next.nodeType === Node.TEXT_NODE && next.textContent.startsWith("\n")) {
      next.textContent = next.textContent.slice(1);
      if (next.textContent === "") next.remove();
    }
    line.remove();
  }

  function hideImports(code) {
    var lines = Array.prototype.slice.call(code.querySelectorAll(":scope > .giallo-l"));
    var hasImports = lines.some(function (l) { return lineText(l).startsWith("import "); });
    if (!hasImports) return;
    var i = 0;
    // Imports are at the top, possibly separated by blank lines.
    while (i < lines.length && (lineText(lines[i]) === "" || lineText(lines[i]).startsWith("import "))) {
      removeLine(lines[i]);
      i++;
    }
  }

  function addCopyButton(pre, rawCode) {
    var wrapper = document.createElement("div");
    wrapper.className = "code-block";
    pre.parentNode.insertBefore(wrapper, pre);
    wrapper.appendChild(pre);

    var button = document.createElement("button");
    button.type = "button";
    button.setAttribute("aria-label", "Copy code to clipboard");
    button.innerHTML = '<svg viewBox="0 0 24 24" class="copy-icon"><use xlink:href="#svg-copy"></use></svg>';
    wrapper.appendChild(button);

    var timeout = null;
    button.addEventListener("click", function () {
      navigator.clipboard.writeText(rawCode).then(function () {
        button.innerHTML = '<svg viewBox="0 0 24 24" class="copy-icon"><use xlink:href="#svg-copied"></use></svg>';
        clearTimeout(timeout);
        timeout = setTimeout(function () {
          button.innerHTML = '<svg viewBox="0 0 24 24" class="copy-icon"><use xlink:href="#svg-copy"></use></svg>';
        }, 1200);
      }, function (err) {
        console.error("Failed to copy code:", err);
      });
    });
  }

  function wrapTables() {
    document.querySelectorAll(".main-content table").forEach(function (table) {
      if (table.parentNode.classList.contains("table-wrapper")) return;
      var wrapper = document.createElement("div");
      wrapper.className = "table-wrapper";
      table.parentNode.insertBefore(wrapper, table);
      wrapper.appendChild(table);
    });
  }

  function init() {
    wrapTables();
    document.querySelectorAll(".main-content pre.giallo").forEach(function (pre) {
      var code = pre.querySelector("code");
      if (!code) return;
      var rawCode = code.textContent;
      // The full code, imports included, for tools that read the page
      // (the OPENRNDR IntelliJ plugin's run buttons).
      code.setAttribute("data-raw-code", rawCode);
      pre.setAttribute("tabindex", "0");
      if (code.getAttribute("data-lang") === "kotlin") hideImports(code);
      addCopyButton(pre, rawCode);
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
