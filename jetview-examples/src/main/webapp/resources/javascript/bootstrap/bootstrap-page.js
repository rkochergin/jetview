const Bootstrap = (() => {

    class Button extends JV.EnhanceMixin(HTMLButtonElement) {

        constructor() {
            super();
            if (this.consumeBooleanAttribute("data-onclick")) {
                this.onclick = () => this.call('onclick');
            }
        }

        update(data) {
            for (const item of data) {
                if (item.property === "text") {
                    this.textContent = item.value;
                } else if (item.property === "style" || item.property === "size" || item.property === "enabled") {
                    if (item.oldValue) {
                        this.classList.remove(item.oldValue);
                    }
                    if (item.newValue) {
                        this.classList.add(item.newValue);
                    }
                } else if (item.event === "onclick") {
                    this.onclick = item.value ? () => this.call('onclick') : null;
                }
            }
        }
    }

    class ToastContainer extends JV.EnhanceMixin(HTMLDivElement) {

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

    class Table extends JV.EnhanceMixin(HTMLDivElement) {

        #body;
        #spinner;
        #observer;
        #offset = 0;
        #limit = 20;

        constructor() {
            super();
            this.#body = this.getElementsByTagName("tbody")[0];
            this.#spinner = this.querySelector(".bs-table-spinner");
        }

        connectedCallback() {
            // Intersection Observer callback
            const observerCallback = (entries, observer) => {
                const [entry] = entries;
                // If the target (loading indicator) is intersecting the viewport, fetch more data
                if (entry.isIntersecting) {
                    // Stop observing temporarily to prevent multiple simultaneous fetches
                    observer.unobserve(this.#spinner);
                    this.fetchRows();
                }
            };

            // Set up the Intersection Observer
            this.#observer = new IntersectionObserver(observerCallback, {
                root: this, // The scrollable area
                rootMargin: '100px',
                threshold: 1.0
            });

            // Start observing the loading indicator
            this.#observer.observe(this.#spinner);

            this.fetchRows();
        }

        update(data) {
            for (const item of data) {
                if (item.property === "rows") {
                    this.#body.insertAdjacentHTML("beforeend", item.markup);
                    this.#offset += this.#limit;
                    if (item.size === this.#limit) {
                        this.showSpinner()
                        this.#observer.observe(this.#spinner);
                    }
                }
            }
        }

        fetchRows() {
            this.showSpinner();
            this.call('fetch', {"offset": this.#offset, "limit": this.#limit});
            this.hideSpinner();
        }

        showSpinner() {
            this.#spinner.classList.remove('d-none');
        }

        hideSpinner() {
            this.#spinner.classList.add('d-none');
        }

    }

    class Progress extends JV.EnhanceMixin(HTMLDivElement) {

        #progressBar;
        #errorListener;
        #percent = 0;
        #completed = true;

        constructor() {
            super();
            this.#progressBar = this.querySelector(".progress-bar");
        }

        connectedCallback() {
            JV.getPushSource().subscribe(this, data => {
                if (data.property === "state") {
                    const oldPercent = this.#percent;
                    this.#percent = data.percent;
                    this.#progressBar.setAttribute("aria-valuemin", `${data.min}`);
                    this.#progressBar.setAttribute("aria-valuemax", `${data.max}`);
                    this.#progressBar.setAttribute("aria-valuenow", `${data.value}`);
                    this.#progressBar.setAttribute("style", `width: ${this.#percent}%`);
                    this.#progressBar.textContent = `${this.#percent}%`;
                    this.#completed = this.#percent > oldPercent && this.#percent >= 100;
                    if (this.#completed) {
                        this.call("complete");
                    }
                }
            })
            this.#errorListener = event => {
                if (event.target.readyState === EventSource.CONNECTING) {
                    if (!this.#completed) {
                        const interval = setInterval(() => {
                            if (event.target.readyState === EventSource.OPEN) {
                                clearInterval(interval);
                            } else if (!this.#completed) {
                                this.call("state");
                            }
                        }, 1000);
                    }
                }
            };
            JV.getPushSource().addErrorListener(this.#errorListener);
        }

        disconnectedCallback() {
            JV.getPushSource().unsubscribe(this);
            JV.getPushSource().removeErrorListener(this.#errorListener);
        }
    }

    return {Button, ToastContainer, Table, Progress};
})();


customElements.define("bs-button", Bootstrap.Button, {extends: "button"});
customElements.define("bs-toast-container", Bootstrap.ToastContainer, {extends: "div"});
customElements.define("bs-table", Bootstrap.Table, {extends: "div"});
customElements.define("bs-progress", Bootstrap.Progress, {extends: "div"});