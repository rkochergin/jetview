const Elements = (() => {

    class Button extends JV.EnhanceMixin(HTMLButtonElement) {

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
            // this.call('attach');
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

customElements.define("jv-button", Elements.Button, {extends: "button"});
