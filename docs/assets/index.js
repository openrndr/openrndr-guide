window.$docsify = {
    repo: '/openrndr',
    name: 'openRNDR',
    auto2top: true,
    loadSidebar: true,
    coverpage: true,
    executeScript: true,
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
        (hook, vm) => {
            const re = /@@(.*?)@@/g;
            const re2 = /\[([^\[]+)\]\(([^\)]+)\)/g;
            const atRe = /@/g;
            let temp = document.createElement('span');

            hook.afterEach((html) => {
                return html.replace(re, (x) => {
                    temp.innerHTML = x.replace(atRe, '');
                    return  temp.innerText.replace(re2, '<a target="_blank" href=\'$2\'>$1</a>');
                })
            })
        }
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