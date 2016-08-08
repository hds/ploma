(ns ploma-front.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-flux.dispatcher :as flux]
            [cljs.nodejs :as nodejs]))

(def fs (nodejs/require "fs"))
(def SQL (nodejs/require "sql.js"))

(def dispatcher (flux/dispatcher))
(defonce app-state (atom {:message "Hello, om world!"
                          :selected-table ""}))

(def dispatch-token
  (flux/register
    dispatcher
    (fn [{:keys [selected-table]}]
      (when selected-table
      (swap! app-state assoc :selected-table selected-table)))))

(defn list-tables [db]
  (mapv (fn [row2] (js/console.log "row2" row2) (get row2 0))
    (mapv (fn [row] (js/console.log "row" row) (.-values row))
      (.exec db "SELECT name FROM sqlite_master WHERE type='table'"))))

(defn select [db query]
  (get (.exec db query) 0))

(defn table-select [db table-name]
  (select db (str "SELECT * FROM " table-name)))

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

(defn list-tables' [db]
  (let [result (get (.exec db "SELECT name FROM sqlite_master WHERE type='table'") 0)]
    (js/console.log "test columns:" (column-names result))
    (js/console.log "test values:" (value-maps result))
  )
  (js/console.log "test exec:" (.exec db "SELECT name FROM sqlite_master WHERE type='table'"))
  (->> (.exec db "SELECT name FROM sqlite_master WHERE type='table'")
    (mapv #(.-values %))
    (mapv #(get % 0))
  ))

(defn list-tables'' [db]
  (let [result (select db "SELECT name FROM sqlite_master WHERE type='table'")]
    (map #(get % "name") (value-maps result))))

(defn select-lorem [db]
  (let [res (.exec db "SELECT * FROM lorem")]
    (let [rows (.-values (nth res 0))]
      (mapv (fn [row]
        (js/console.log (get row 0))
        (get row 0)
        ) rows))))

(defn open-db [filename]
  (def filebuffer (.readFileSync fs filename))
  (let [db (let [Database (.-Database SQL)] (Database. filebuffer))]
    (js/console.log db)
    db))

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

(defn select-table [table-name]
  (flux/dispatch dispatcher {:selected-table table-name}))

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
                      #js {:onClick (fn [event] (js/console.log "Selecting table:" table-name) (select-table table-name))}
                      table-name)))
            tables)))))))

(defn mount-root [setting]
  (om/root
    (fn [state owner]
      (let [db (open-db "test.sqlite")
            rows (select-lorem db)
            tables (list-tables'' db)
            selected-table (get state :selected-table)]
        (js/console.log "tables:" (list-tables' db))
        (js/console.log (type owner))
        (reify om/IRender
          (render [_]
            (dom/div #js {:className "outer-wrapper"}
              (om/build table-view {:tables tables :selected-table selected-table} owner)
              (if-not (empty? selected-table)
                (om/build table-content-view (table-select db selected-table) owner)
                (dom/div #js {:className "no-table-selected"} nil))
            )))))
   app-state
   {:target (. js/document
               (getElementById "app"))}))

(defn init! [setting]
  (mount-root setting))
