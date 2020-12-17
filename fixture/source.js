$package("js.dom");

/**
 * General push button.
 * 
 * @author Iulian Rotaru
 * @since 1.3
 * @constructor Construct button instance.
 * 
 * @param js.dom.Document ownerDoc element owner document,
 * @param Node node native {@link Node} instance.
 * @assert <code>ownerDoc</code> argument is not undefined or null and is instance of {@link js.dom.Document} and
 *         <code>node</code> is a button.
 */
js.dom.Button = function (ownerDoc, node) {
    $assert(this instanceof js.dom.Button, "js.dom.Button#Button", "Invoked as function.");
    $assert(node.nodeName.toLowerCase() === "button", "js.dom.Button#Button", "Node is not an input.");
    this.$super(ownerDoc, node);
};

