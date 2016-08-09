(ns ploma-front.store
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-flux.dispatcher :as flux]
            [cljs.nodejs :as nodejs]))


(defonce app-state (atom {:message "Hello, om world!"
                          :selected-table ""}))
