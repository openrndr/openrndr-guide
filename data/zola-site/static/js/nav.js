/*
 * Sidebar navigation: expand/collapse buttons and the mobile menu.
 * The items leading to the current page are marked `active` by the
 * templates, so they are expanded without JavaScript.
 */
(function () {
  "use strict";

  function init() {
    document.addEventListener("click", function (e) {
      var expander = e.target.closest(".nav-list-expander");
      if (!expander) return;
      e.preventDefault();
      var open = expander.parentNode.classList.toggle("active");
      expander.setAttribute("aria-expanded", open);
    });

    var siteNav = document.getElementById("site-nav");
    var mainHeader = document.getElementById("main-header");
    var menuButton = document.getElementById("menu-button");
    if (menuButton) {
      menuButton.addEventListener("click", function (e) {
        e.preventDefault();
        var open = menuButton.classList.toggle("nav-open");
        siteNav.classList.toggle("nav-open", open);
        mainHeader.classList.toggle("nav-open", open);
        menuButton.setAttribute("aria-expanded", open);
      });
    }

    // Keep the link to the current page visible in the sidebar
    var current = siteNav && siteNav.querySelector("a.nav-list-link.active");
    if (current) {
      current.scrollIntoView({ block: "center" });
    }
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
