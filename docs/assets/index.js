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
            '/': 'Search'
        }
    },
    plugins: [
        function(hook, vm) {
            var re = /@@(.*?)@@/g;
            var re2 = /\[([^\[]+)\]\(([^\)]+)\)/g;
            var atRe = /@/g;
            var temp = document.createElement('span');
            hook.afterEach(function (html) {
                return html.replace(re, function (x) {
                    temp.innerHTML = x.replace(atRe, '');
                    var text = temp.innerText;
                    return  text.replace(re2, '<a target="_blank" href=\'$2\'>$1</a>');
                })
            })
        }
    ]
};