const configs = {
    repoLink: 'https://github.com/openrndr/openrndr-book-docsify'
};


window.$docsify = {
    repo: '/openrndr',
    name: 'openRNDR',
    auto2top: true,
    loadSidebar: true,
    coverpage: true,
    executeScript: true,
    formatUpdated: '{MM}/{DD} {HH}:{mm}',
    search: {
        noData: {
            '/': 'No results!'
        },
        paths: 'auto',
        placeholder: {
            '/': 'Search ...'
        }
    },
    plugins: [
        plugins.linkify,
        plugins.editLink
    ]
};

const invertPage = () => {
    if(document.body.classList.contains('inverted')){
        document.body.classList.remove('inverted');
    }else{
        document.body.classList.add('inverted');
    }
};

window.onload = ()=>{
    const invertHandler = document.createElement('span');
    invertHandler.innerHTML = "invert";
    invertHandler.className = "invert-handler";
    invertHandler.onclick = invertPage;
    document.body.appendChild(invertHandler);
};