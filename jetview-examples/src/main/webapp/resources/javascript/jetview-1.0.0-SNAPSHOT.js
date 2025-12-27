const JV = (() => {
    const servletPath = getServletPath();

    const call = (id, event, data, callback) => {
        const xHttp = new XMLHttpRequest();
        xHttp.onload = function () {
            try {
                const json = JSON.parse(this.responseText);
                json.forEach((item) => {
                    const e = findElementByJetViewId(item.id);
                    if (e.tagName === "BODY") {
                        e.innerHTML = unescapeHTML(item.text);
                    } else {
                        e.outerHTML = unescapeHTML(item.text);
                        initJs(findElementByJetViewId(item.id));
                    }
                })
                if (callback) {
                    callback();
                }
            } catch (e) {
                console.warn(e);
                // location.reload();
            }
        };
        const uri = (servletPath.endsWith("/") ? servletPath : servletPath + "/") + "jetview-ajax-page";
        xHttp.open("POST", uri, true);
        xHttp.setRequestHeader("Content-Type", "application/json");
        xHttp.send(JSON.stringify({"id": id, "event": event, "data": data}));
    }

    function findElementByJetViewId(jvId) {
        return document.querySelector(`[data-jv-id='${jvId}']`);
    }

    function initJs(element) {
        const component = element.dataset.jvJs;
        if (component) {
            const namespaces = component.split(".");
            const context = namespaces.shift()
            const func = namespaces.length > 0 ? namespaces.join(".") + ".init" : "init";
            executeFunctionByName(func, eval(context), element);
            delete element.dataset.jvJs;
        }
    }

    function getServletPath() {
        const attrName = 'data-servlet-path';
        const e = document.querySelector(`meta[${attrName}]`);
        return e ? e.getAttribute(attrName) : "/";
    }

    function unescapeHTML(str) {
        return str.replaceAll(
            /&amp;|&lt;|&gt;|&#39;|&quot;/g,
            tag =>
                ({
                    '&amp;': '&',
                    '&lt;': '<',
                    '&gt;': '>',
                    '&#39;': "'",
                    '&quot;': '"'
                }[tag] || tag)
        );
    }

    function executeFunctionByName(functionName, context) {
        const args = Array.prototype.slice.call(arguments, 2);
        const namespaces = functionName.split(".");
        const func = namespaces.pop();
        for (const element of namespaces) {
            context = context[element];
        }
        return context[func](...args);
    }

    return {call, initJs};
})();

window.onload = () => {
    const elements = document.querySelectorAll('[data-jv-js]');
    elements.forEach(element => {
        JV.initJs(element);
    });
};
