const TodoPage = (() => {

    const AddForm = (() => {
        function submitForm(componentId, evt) {
            evt.preventDefault();
            const form = evt.target;
            const formData = new FormData(form)
            JV.call(componentId, 'onSubmit', Object.fromEntries(formData));
            form.reset();
        }
        return {submitForm};
    })();

    const TodoListItem = (() => {
        function startEditing(listItemId) {
            const editButton = document.getElementById("todo-edit-button-" + listItemId);
            const input = document.getElementById("todo-input-" + listItemId);
            editButton.style.display = "none";
            input.removeAttribute("readonly");
            input.focus();
            input.setSelectionRange(input.value.length, input.value.length);
        }
        function stopEditing(listItemId) {
            const editButton = document.getElementById("todo-edit-button-" + listItemId);
            editButton.style.display = "block";
            const input = document.getElementById("todo-input-" + listItemId);
            input.setAttribute("readonly", "");
        }
        function completeTodo(listItemId, completed) {
            const data = {'completed': completed};
            JV.call(listItemId, 'onComplete', data);
        }
        function submitForm(listItemId, evt) {
            evt.preventDefault();
            const form = evt.target;
            const formData = new FormData(form)
            const data = Object.fromEntries(formData);
            JV.call(listItemId, 'onChange', data);
        }
        function deleteTodo(listItemId) {
            JV.call(listItemId, 'onDelete');
        }
        return {startEditing, stopEditing, completeTodo, submitForm, deleteTodo};
    })();

    return {AddForm,TodoListItem};
})();