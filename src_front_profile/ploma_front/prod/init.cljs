(ns ploma-front.init
    (:require [ploma-front.core :as core]
              [ploma-front.conf :as conf]))

(enable-console-print!)

(defn start-descjop! []
  (core/init! conf/setting))

(start-descjop!)
