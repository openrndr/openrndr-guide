const configs = {
    repoLink: 'https://github.com/openrndr/openrndr-guide',

};


window.$docsify = {
    name: 'OPENRNDR GUIDE',
    auto2top: true,
    loadSidebar: true,
    maxLevel: 2,
    subMaxLevel: 2,
    coverpage: false,
    executeScript: true,
    autoHeader: true,
    formatUpdated: '{DD}-{MM}-{YYYY}',
    search: {
        noData: {
            '/': 'No results'
        },
        paths: 'auto',
        placeholder: {
            '/': 'Search'
        }
    },
    ga: 'UA-109731993-1',
    plugins: [
        plugins.linkify,
        plugins.editLink,
        // window.DocsifyCopyCodePlugin.init()
    ],
    homepage: '01_Before_you_start/C00WhatIsOPENRNDR.md',
    themeColor: '#fdd0dd',
};



const setBodyHeight = () => {
  setTimeout(()=>{
    const main = document.querySelector('#main');
    const height = Math.max(main.offsetHeight + 200, window.innerHeight);
    document.body.style.height = `${height}px`;
  }, 200);
};

window.onload = () => {
    setBodyHeight();
    document.body.classList.add("loaded");
};


window.onhashchange = function(e) {
  setBodyHeight();
};


