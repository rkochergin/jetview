const JV = (() => {
    const servletPath = getServletPath();

    const call = (id, event, data, callback) => {
        const xHttp = new XMLHttpRequest();
        xHttp.onload = function () {
            try {
                const json = JSON.parse(this.responseText);
                json.forEach((item) => {
                    const e = document.querySelector(`[data-jv-id='${item.id}']`);
                    if (e.tagName === "BODY") {
                        e.innerHTML = unescapeHTML(item.text);
                    } else {
                        e.outerHTML = unescapeHTML(item.text);
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

    function getServletPath() {
        const attrName = 'data-servlet-path';
        const e = document.querySelector(`meta[${attrName}]`);
        return e ? e.getAttribute(attrName) : "/";
    }

    function unescapeHTML(str) {
        return str.replace(
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

    return {call};
})();
