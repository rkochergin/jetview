const JV = (() => {
    const servletPath = getServletPath();

    const call = (id, event, data) => {
        const xHttp = new XMLHttpRequest();
        xHttp.onload = function () {
            try {
                if (!this.responseText) {
                    return;
                }
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

    function getUri() {
        return (servletPath.endsWith("/") ? servletPath : servletPath + "/") + "jetview-ajax";
    }

    function getPushUri() {
        return "/jetview-push";
    }

    function getPushSource() {
        if (!this.pushSource) {
            this.pushSource = new PushSource(getPushUri());
        }
        return this.pushSource;
    }

    addEventListener('DOMContentLoaded', () => {
        call(null, "DOMContentLoaded");
    });

    class PushSource {

        #eventSource;
        #subscribers;
        #connectionOpenListeners;
        #connectionErrorListeners;

        constructor(pushUri) {
            this.#subscribers = new Map();
            this.#connectionOpenListeners = new Set();
            this.#connectionErrorListeners = new Set();
            const jvId = document.body.getAttribute("data-jv-id");
            this.#eventSource = new EventSource(`${pushUri}?id=${jvId}`);
            this.#eventSource.addEventListener("message", event => {
                const data = JSON.parse(event.data);
                const componentId = data.id;
                const message = data.message;
                const subscriber = this.#subscribers.get(componentId);
                if (subscriber) {
                    subscriber(message);
                }
            });
            this.#eventSource.addEventListener("open", event => {
                this.#connectionOpenListeners.forEach(listener => listener(event));
            });
            this.#eventSource.addEventListener("error", event => {
                this.#connectionErrorListeners.forEach(listener => listener(event));
            });
        }

        subscribe(enhanceMixin, listener) {
            this.#subscribers.set(enhanceMixin.getJvId(), listener);
        }

        unsubscribe(enhanceMixin) {
            this.#subscribers.delete(enhanceMixin.getJvId());
        }

        addConnectionOpenListener(listener) {
            this.#connectionOpenListeners.add(listener);
        }

        removeConnectionOpenListener(listener) {
            this.#connectionOpenListeners.delete(listener);
        }

        addConnectionErrorListener(listener) {
            this.#connectionErrorListeners.add(listener);
        }

        removeConnectionErrorListener(listener) {
            this.#connectionErrorListeners.delete(listener);
        }
    }

    const EnhanceMixin = (superclass) => {
        return class extends superclass {

            getJvId() {
                return this.dataset.jvId;
            }

            connectedCallback() {
                console.log(`Element '${this.getJvId()}' connected to DOM.`);
            }

            disconnectedCallback() {
                console.log(`Element '${this.getJvId()}' disconnected from DOM.`);
            }

            connectedMoveCallback() {
                console.log(`Element '${this.getJvId()}' moved with moveBefore()`);
            }

            adoptedCallback() {
                console.log(`Element '${this.getJvId()}' moved to a new parent.`);
            }

            attributeChangedCallback(name, oldValue, newValue) {
                console.log(`Element '${this.getJvId()}' attribute '${name}' has changed from '${oldValue}' to '${newValue}'.`);
            }

            update(data) {
                console.log(`Element '${this.getJvId()}' update data '${JSON.stringify(data)}' received.`);
            }

            call(event, data) {
                JV.call(this.getJvId(), event, data);
            }

            consumeAttribute(attributeName) {
                const attribute = this.getAttribute(attributeName);
                this.removeAttribute(attributeName);
                return attribute;
            }

            consumeBooleanAttribute(attributeName) {
                const result = this.hasAttribute(attributeName);
                this.removeAttribute(attributeName);
                return result;
            }

            selectKeys(obj, ...keys) {
                return Object.fromEntries(
                    keys
                        .filter(key => key in obj)
                        .map(key => [key, obj[key]])
                );
            }
        };
    };

    return {call, EnhanceMixin, getPushSource};
})();

