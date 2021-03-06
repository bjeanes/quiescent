(ns quiescent.dom.uncontrolled
  (:require [goog.object :as gobj]
            [quiescent.dom :as dom]
            [cljsjs.react]))

(let [reset-value (fn []
                    (this-as this
                             (let [props (.-props this)
                                   node (js/React.findDOMNode this)]
                               (when (and (.-value props)
                                          (not= (.-value node) (.-value props)))
                                 (set! (.-value node) (.-value props))))))]
  (defn uncontrolled-component
    "Delegate should be a valid argument to React.createElement (that
  is, a tag name or a ReactClass.)

  Returns a ReactJS class that behaves in all respects as the
  delegate, with the exception that setting the 'value' property will
  not cause the element to become a controlled component."
    [name delegate]
    (js/React.createClass
     (clj->js {:getDisplayName (fn [] (str "uncontrolled-" name))
               :render (fn []
                         (this-as this
                                  (let [new-props (gobj/clone (.-props this))]
                                    (js-delete new-props "value")
                                    (js/React.createElement delegate new-props))))
               :componentDidUpdate reset-value
               :componentDidMount reset-value}))))

(def input (dom/constructor (uncontrolled-component "input" "input")))
(def textarea (dom/constructor (uncontrolled-component "textarea" "textarea")))
(def option (dom/constructor (uncontrolled-component "option" "option")))
