const plugins = {
    linkify:
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
        },

    editLink:
        (hook, vm) => {
            hook.beforeEach((html) => {
                let url = `${configs.repoLink}/blob/master/docs/${vm.route.file}`;
                return html + '\n----\n'
                    + '<div class="editpage">'
                    + 'Last modified {docsify-updated}'
                    + '<a href=' + url + '> Improve this page</a>'
                    + '</div>'
            })
        }
};