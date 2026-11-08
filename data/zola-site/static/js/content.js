/*
 * Tables: wrapped in a div.table-wrapper like just-the-docs does, which
 * styles them and makes wide tables scroll horizontally on small screens.
 *
 * Code blocks:
 * - Kotlin programs include their import statements so they can be copied
 *   and run. The imports are hidden on the page to keep examples short.
 * - Every code block gets a copy button which copies the full code,
 *   including the hidden imports.
 * - In the OPENRNDR IntelliJ plugin's guide tab, Kotlin programs (with a
 *   fun main) also get a run button, which runs them in the IDE.
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

  // Wraps pre in a div.code-block with a copy button; returns the wrapper.
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
    return wrapper;
  }

  /*
   * Run buttons for the OPENRNDR IntelliJ plugin, which shows the guide in
   * an IDE tab and gives its pages window.openrndrIde (see run-buttons.js in
   * https://github.com/openrndr/openrndr-intellij). The plugin's side is only
   *   window.openrndrIde.runProgram(index, title, code)
   *     index: the block's position among the page's Kotlin blocks, from 1
   *     title: document.title
   *     code:  the full code, imports included
   * and an "openrndr-ide-ready" event on window once openrndrIde is there.
   * The plugin adds it after the page has loaded, but either order works.
   * Outside the IDE there is no openrndrIde and no run buttons.
   *
   * Keep this contract when the guide's markup or site generator changes,
   * and the plugin needs no update.
   */
  var kotlinBlocks = []; // { wrapper, code, index }
  var contentReady = false;
  var runButtonsAdded = false;

  function addRunButtons() {
    var ide = window.openrndrIde;
    if (!contentReady || runButtonsAdded || !ide || typeof ide.runProgram !== "function") return;
    runButtonsAdded = true;
    kotlinBlocks.forEach(function (block) {
      if (!/\bfun\s+main\s*\(/.test(block.code)) return;
      var button = document.createElement("button");
      button.type = "button";
      button.className = "run-button";
      button.setAttribute("aria-label", "Run this program in the IDE");
      button.title = "Run this program in the IDE";
      button.innerHTML = '<svg viewBox="0 0 24 24"><path d="M6 3.5v17l14-8.5z"></path></svg>';
      button.addEventListener("click", function () {
        window.openrndrIde.runProgram(block.index, document.title, block.code);
      });
      block.wrapper.appendChild(button);
    });
  }

  window.addEventListener("openrndr-ide-ready", addRunButtons);

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
      var kotlin = code.getAttribute("data-lang") === "kotlin";
      pre.setAttribute("tabindex", "0");
      if (kotlin) hideImports(code);
      var wrapper = addCopyButton(pre, rawCode);
      if (kotlin) kotlinBlocks.push({ wrapper: wrapper, code: rawCode, index: kotlinBlocks.length + 1 });
    });
    contentReady = true;
    addRunButtons();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
