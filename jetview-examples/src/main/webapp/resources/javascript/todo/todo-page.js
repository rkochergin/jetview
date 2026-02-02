(() => {

    class AddForm extends JV.EnhanceMixin(HTMLFormElement) {
        constructor() {
            super();
            this.addEventListener('submit', evt => this.submit(evt));
        }

        submit(evt) {
            evt.preventDefault();
            const form = evt.target;
            const formData = new FormData(form)
            this.call('onSubmit', Object.fromEntries(formData));
            form.reset();
        }
    }

    class TodoListItem extends JV.EnhanceMixin(HTMLLIElement) {

        #form
        #inputField;
        #editButton;
        #deleteButton;
        #checkboxField;

        constructor() {
            super();
            this.#form = this.querySelector("[data-form]");
            this.#form.addEventListener('submit', evt => this.changeTodo(evt));
            this.#inputField = this.querySelector("[data-input-field]");
            this.#inputField.addEventListener("dblclick", () => this.startEditing());
            this.#inputField.addEventListener("blur", () => this.stopEditing())
            this.#editButton = this.querySelector("[data-edit-button]");
            this.#editButton.addEventListener('click', () => this.startEditing());
            this.#deleteButton = this.querySelector("[data-delete-button]");
            this.#deleteButton.addEventListener('click', () => this.deleteTodo());
            this.#checkboxField = this.querySelector("[data-checkbox-field]");
            this.#checkboxField.addEventListener('click', () => this.completeTodo());
        }

        startEditing() {
            this.#editButton.style.display = "none";
            this.#inputField.removeAttribute("readonly");
            this.#inputField.focus();
            this.#inputField.setSelectionRange(this.#inputField.value.length, this.#inputField.value.length);
        }

        stopEditing() {
            this.#editButton.style.display = "block";
            this.#inputField.setAttribute("readonly", "");
        }

        changeTodo(evt) {
            evt.preventDefault();
            const form = evt.target;
            const formData = new FormData(form)
            const data = Object.fromEntries(formData);
            this.call('onChange', data);
        }

        completeTodo() {
            const data = {'completed': this.#checkboxField.checked};
            this.call('onComplete', data);
        }

        deleteTodo() {
            this.call('onDelete');
        }
    }

    customElements.define("todo-add-form", AddForm, {extends: "form"});
    customElements.define("todo-list-item", TodoListItem, {extends: "li"});

})();
