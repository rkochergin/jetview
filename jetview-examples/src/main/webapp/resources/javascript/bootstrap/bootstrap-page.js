const Bootstrap = (() => {

    class Button extends JVComponents.EnhanceMixin(HTMLButtonElement) {

        connectedCallback() {
            if (this.consumeBooleanAttribute("data-onclick")) {
                this.onclick = () => this.call('onclick');
            }
        }

    }

    class ToastContainer extends JVComponents.EnhanceMixin(HTMLDivElement) {

        static Position = {
            TOP_LEFT: "top-0 start-0",
            TOP_CENTER: "top-0 start-50 translate-middle-x",
            TOP_RIGHT: "top-0 end-0",
            MIDDLE_LEFT: "top-50 start-0 translate-middle-y",
            MIDDLE_CENTER: "top-50 start-50 translate-middle",
            MIDDLE_RIGHT: "top-50 end-0 translate-middle-y",
            BOTTOM_LEFT: "bottom-0 start-0",
            BOTTOM_CENTER: "bottom-0 start-50 translate-middle-x",
            BOTTOM_RIGHT: "bottom-0 end-0"
        };

        connectedCallback() {
            this.position = ToastContainer.Position[this.consumeAttribute("data-jv-position")] || ToastContainer.Position.BOTTOM_RIGHT;
        }

        set position(position) {
            this.className = `toast-container position-fixed ${position} p-3`;
        }

        update(data) {
            for (const item of data) {
                if (item.property === "position") {
                    this.position = ToastContainer.Position[item.newValue];
                }
                if (item.action === "show") {
                    this.insertAdjacentHTML("beforeend", item.markup);
                    const toastElement = this.lastElementChild;
                    const toast = new bootstrap.Toast(toastElement);
                    const toastId = item.toastId;
                    toastElement.addEventListener('hidden.bs.toast', () => {
                        toastElement.remove();
                        this.call('onClosed', {"toastId": toastId});
                    })
                    toast.show()
                }
            }
        }

    }

    // Create a class for the element
    class MyCustomElement extends JVComponents.EnhanceMixin(HTMLDivElement) {

        constructor() {
            super();

            this.innerHTML = "<h1>Hello, World!</h1>";

            // const shadow = this.attachShadow({mode: "open"});

            // const buttonElement = document.createElement("button");
            // buttonElement.textContent = "Hello, World!";
            // buttonElement.onclick = () => this.setAttribute("size", "120");
            //
            // this.appendChild(buttonElement);
        }

        connectedCallback() {
            super.connectedCallback();
            this.call('attach', {"jvId": this.getJvId()});
        }

    }

    return {Button, MyCustomElement, ToastContainer};
})();


customElements.define("bs-button", Bootstrap.Button, {extends: "button"});
customElements.define("bs-toast-container", Bootstrap.ToastContainer, {extends: "div"});
customElements.define("bs-page-custom-component", Bootstrap.MyCustomElement, {extends: "div"});