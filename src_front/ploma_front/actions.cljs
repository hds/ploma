(ns ploma-front.actions
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-flux.dispatcher :as flux]
            [cljs.nodejs :as nodejs]
            [ploma-front.store :as store]))

(def dispatcher (flux/dispatcher))

(def dispatch-token
  (flux/register
    dispatcher
    (fn [{:keys [selected-table]}]
      (when selected-table
      (swap! store/app-state assoc :selected-table selected-table)))))

(defn select-table [table-name]
  (flux/dispatch dispatcher {:selected-table table-name}))
