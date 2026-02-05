const Elements = (() => {

    class Button extends JV.EnhanceMixin(HTMLButtonElement) {

        static #LISTENERS_NS = "data-jv-listener-";

        #handlers = new Map();

        constructor() {
            super();
            this.getAttributeNames().forEach(name => {
                if (name.startsWith(Button.#LISTENERS_NS)) {
                    const eventPropertyRequirements = this.consumeAttribute(name);
                    const eventType = name.replace(Button.#LISTENERS_NS, "")
                    this.setEventHandler(eventType, eventPropertyRequirements.split(","));
                }
            });
        }

        connectedCallback() {
            super.connectedCallback();
            // this.call('attach');
        }

        setEventHandler(eventType, eventPropertyRequirements) {
            const handler = eventPropertyRequirements ?
                evt => this.call(eventType, this.selectKeys(evt, ...eventPropertyRequirements)) :
                () => this.call(eventType);
            this.addEventListener(eventType, handler);
            this.#handlers.set(eventType, handler)
        }

        removeEventHandler(eventType) {
            const handler = this.#handlers.get(eventType);
            if (handler) {
                this.removeEventListener(eventType, handler);
                this.#handlers.delete(eventType);
            }
        }

        update(data) {
            for (const item of data) {
                if (item.js) {
                    eval(item.js);
                }
            }
        }

    }

    return {Button};
})();

customElements.define("jv-el-button", Elements.Button, {extends: "button"});
