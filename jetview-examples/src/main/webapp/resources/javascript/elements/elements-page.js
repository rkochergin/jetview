const Elements = (() => {

    const ElementMixin = (superclass) => {
        return class extends JV.EnhanceMixin(superclass) {

            #handlers = new Map();

            constructor() {
                super();
                this.getAttributeNames().forEach(name => {
                    if (name.startsWith(this.constructor.LISTENERS_NS)) {
                        const eventPropertyRequirements = this.consumeAttribute(name);
                        const eventType = name.replace(this.constructor.LISTENERS_NS, "")
                        this.setEventHandler(eventType, eventPropertyRequirements.split(","));
                    }
                });
            }

            static get LISTENERS_NS() {
                return "data-jv-listener-";
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

        };
    };

    class Html extends ElementMixin(HTMLHtmlElement) {
    }

    class Head extends ElementMixin(HTMLHeadElement) {
    }

    class Body extends ElementMixin(HTMLBodyElement) {
    }

    class Style extends ElementMixin(HTMLStyleElement) {
    }

    class Script extends ElementMixin(HTMLScriptElement) {
    }

    class Div extends ElementMixin(HTMLDivElement) {
    }

    class Span extends ElementMixin(HTMLSpanElement) {
    }

    class Paragraph extends ElementMixin(HTMLParagraphElement) {
    }

    class Heading1 extends ElementMixin(HTMLHeadingElement) {
    }

    class Heading2 extends ElementMixin(HTMLHeadingElement) {
    }

    class Heading3 extends ElementMixin(HTMLHeadingElement) {
    }

    class Heading4 extends ElementMixin(HTMLHeadingElement) {
    }

    class Heading5 extends ElementMixin(HTMLHeadingElement) {
    }

    class Heading6 extends ElementMixin(HTMLHeadingElement) {
    }

    class Anchor extends ElementMixin(HTMLAnchorElement) {
    }

    class Image extends ElementMixin(HTMLImageElement) {
    }

    class HR extends ElementMixin(HTMLHRElement) {
    }

    class BR extends ElementMixin(HTMLBRElement) {
    }

    class Form extends ElementMixin(HTMLFormElement) {
    }

    class Input extends ElementMixin(HTMLInputElement) {
    }

    class Button extends ElementMixin(HTMLButtonElement) {
    }

    class Select extends ElementMixin(HTMLSelectElement) {
    }

    class TextArea extends ElementMixin(HTMLTextAreaElement) {
    }

    class Label extends ElementMixin(HTMLLabelElement) {
    }

    class UL extends ElementMixin(HTMLUListElement) {
    }

    class OL extends ElementMixin(HTMLOListElement) {
    }

    class LI extends ElementMixin(HTMLLIElement) {
    }

    class Table extends ElementMixin(HTMLTableElement) {
    }

    class TableRow extends ElementMixin(HTMLTableRowElement) {
    }

    class TableHeaderCell extends ElementMixin(HTMLTableCellElement) {
    }

    class TableDataCell extends ElementMixin(HTMLTableCellElement) {
    }

    return {
        Html, Head, Body, Style, Script, Div, Span, Paragraph, Heading1, Heading2,
        Heading3, Heading4, Heading5, Heading6, Anchor, Image, HR, BR,
        Form, Input, Button, Select, TextArea, Label, UL, OL, LI,
        Table, TableRow, TableHeaderCell, TableDataCell
    };
})();

customElements.define("jv-el-html", Elements.Html, {extends: "html"});
customElements.define("jv-el-head", Elements.Head, {extends: "head"});
customElements.define("jv-el-body", Elements.Body, {extends: "body"});
customElements.define("jv-el-style", Elements.Style, {extends: "style"});
customElements.define("jv-el-script", Elements.Script, {extends: "script"});
customElements.define("jv-el-div", Elements.Div, {extends: "div"});
customElements.define("jv-el-span", Elements.Span, {extends: "span"});
customElements.define("jv-el-p", Elements.Paragraph, {extends: "p"});
customElements.define("jv-el-h1", Elements.Heading1, {extends: "h1"});
customElements.define("jv-el-h2", Elements.Heading2, {extends: "h2"});
customElements.define("jv-el-h3", Elements.Heading3, {extends: "h3"});
customElements.define("jv-el-h4", Elements.Heading4, {extends: "h4"});
customElements.define("jv-el-h5", Elements.Heading5, {extends: "h5"});
customElements.define("jv-el-h6", Elements.Heading6, {extends: "h6"});
customElements.define("jv-el-a", Elements.Anchor, {extends: "a"});
customElements.define("jv-el-img", Elements.Image, {extends: "img"});
customElements.define("jv-el-hr", Elements.HR, {extends: "hr"});
customElements.define("jv-el-br", Elements.BR, {extends: "br"});
customElements.define("jv-el-form", Elements.Form, {extends: "form"});
customElements.define("jv-el-input", Elements.Input, {extends: "input"});
customElements.define("jv-el-button", Elements.Button, {extends: "button"});
customElements.define("jv-el-select", Elements.Select, {extends: "select"});
customElements.define("jv-el-textarea", Elements.TextArea, {extends: "textarea"});
customElements.define("jv-el-label", Elements.Label, {extends: "label"});
customElements.define("jv-el-ul", Elements.UL, {extends: "ul"});
customElements.define("jv-el-ol", Elements.OL, {extends: "ol"});
customElements.define("jv-el-li", Elements.LI, {extends: "li"});
customElements.define("jv-el-table", Elements.Table, {extends: "table"});
customElements.define("jv-el-tr", Elements.TableRow, {extends: "tr"});
customElements.define("jv-el-th", Elements.TableHeaderCell, {extends: "th"});
customElements.define("jv-el-td", Elements.TableDataCell, {extends: "td"});
