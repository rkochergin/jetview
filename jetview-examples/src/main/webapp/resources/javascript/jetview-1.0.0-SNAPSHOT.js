const JV = (() => {
    const servletPath = getServletPath();

    const call = (id, event, data, callback) => {
        const xHttp = new XMLHttpRequest();
        xHttp.onload = function () {
            try {
                const json = JSON.parse(this.responseText);
                json.forEach((item) => {
                    const e = findElementByJetViewId(item.id);
                    const list = item.data;
                    if (list.length === 1 && Object.keys(list[0]).length === 1 && "default_markup" in list[0]) {
                        e.outerHTML = list[0]["default_markup"];
                    } else {
                        e.update(list);
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
        xHttp.open("POST", getUri(), true);
        xHttp.setRequestHeader("Content-Type", "application/json");
        xHttp.send(JSON.stringify({"id": id, "event": event, "data": data}));
    }

    function findElementByJetViewId(jvId) {
        return document.querySelector(`[data-jv-id='${jvId}']`);
    }

    function getServletPath() {
        const attrName = 'data-jv-servlet-path';
        const e = document.querySelector(`meta[${attrName}]`);
        return e ? e.getAttribute(attrName) : "/";
    }

    function getUri(){
        return (servletPath.endsWith("/") ? servletPath : servletPath + "/") + "jetview-ajax";
    }

    function getPushUri(){
        return "/jetview-push";
    }

    window.addEventListener('DOMContentLoaded', () => {
        call(null, "DOMContentLoaded");
    });

    return {call, getPushUri};
})();

