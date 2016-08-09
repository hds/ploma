(ns ploma-front.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-flux.dispatcher :as flux]
            [cljs.nodejs :as nodejs]
            [ploma-front.store :as store]
            [ploma-front.components :as components]
            ))

(def fs (nodejs/require "fs"))
(def SQL (nodejs/require "sql.js"))

(defn select [db query]
  (get (.exec db query) 0))

(defn table-select [db table-name]
  (select db (str "SELECT * FROM " table-name)))

(defn list-tables [db]
  (let [result (select db "SELECT name FROM sqlite_master WHERE type='table'")]
    (map #(get % "name") (components/value-maps result))))

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

(defn mount-root [setting]
  (om/root
    (fn [state owner]
      (let [db (open-db "test.sqlite")
            tables (list-tables db)
            selected-table (get state :selected-table)]
        (reify om/IRender
          (render [_]
            (dom/div #js {:className "outer-wrapper"}
              (om/build components/table-view {:tables tables :selected-table selected-table} owner)
              (if-not (empty? selected-table)
                (om/build components/table-content-view (table-select db selected-table) owner)
                (dom/div #js {:className "no-table-selected"} nil))
            )))))
   store/app-state
   {:target (. js/document
               (getElementById "app"))}))

(defn init! [setting]
  (mount-root setting))
