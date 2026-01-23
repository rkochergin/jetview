const JVComponents = (() => {

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

    class Button extends EnhanceMixin(HTMLButtonElement) {

        static #LISTENERS_NS = "data-jv-listener-";

        constructor() {
            super();
            this.getAttributeNames().forEach(name => {
                if (name.startsWith(Button.#LISTENERS_NS)) {
                    if (this.consumeBooleanAttribute(name)) {
                        const eventName = name.replace(Button.#LISTENERS_NS, "")
                        this.addEventListener(eventName, evt => this.call(eventName));
                    }
                }
            });
            // if (this.consumeBooleanAttribute("data-jv-onclick")) {
            //     this.onclick = evt => {
            //         this.call('onclick', this.selectKeys(evt, 'altKey', 'ctrlKey', 'metaKey', 'shiftKey'));
            //     };
            // }
        }

        connectedCallback() {
            super.connectedCallback();
            this.call('attach');
        }

        update(data) {
            for (const item of data) {
                if (item.js) {
                    eval(item.js);
                }
            }
        }

    }

    customElements.define("jv-button", Button, {extends: "button"});

    return {EnhanceMixin};
})();
