(ns ploma-front.components
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-flux.dispatcher :as flux]
            [cljs.nodejs :as nodejs]
            [ploma-front.actions :as actions]))

(defn column-names [query-result]
  (.-columns query-result))

(defn value-maps [query-result]
  (let [columns (column-names query-result)]
    (map
      (fn [row]
        (js/console.log "columns & row values:" columns row)
        (let [rowmap (zipmap columns row)]
          (js/console.log "Rowmap:" (.-name rowmap)))
        (js/console.log "zip:" (count (zipmap columns row)))
        (zipmap columns row))
      (.-values query-result))))

(defn table-content-view [table-select owner]
  (let [columns (column-names table-select)]
    (reify
      om/IRender
      (render [_]
        (dom/div #js {:className "table-detail"}
          (dom/table nil
            (dom/thead nil
              (apply dom/tr nil
                (map #(dom/th nil (dom/div nil %)) columns)))
            (apply dom/tbody nil
              (map
                (fn [row]
                  (apply dom/tr nil
                    (map #(dom/td nil (get row %)) columns)))
                (value-maps table-select)))))))))

; (defn select-table [table-name]
;   (flux/dispatch dispatcher {:selected-table table-name}))

(defn table-view [{:keys [tables selected-table]} owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:className "table-list"}
        (dom/div #js {:className "filter"}
          (dom/input #js {:type "text" :name "filter" :placeholder "Filter"}))
        (dom/table nil
          (dom/thead nil
            (dom/tr nil
              (dom/th nil "Tables")))
          (apply dom/tbody nil
            (map
              (fn [table-name]
                (dom/tr
                  #js {:className (if (= table-name selected-table) "selected" "")}
                    (dom/td
                      #js {:onClick (fn [event] (js/console.log "Selecting table:" table-name) (actions/select-table table-name))}
                      table-name)))
            tables)))))))
