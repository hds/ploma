(ns ploma.menu
    (:require [cljs.nodejs :as nodejs]))

(def Electron (nodejs/require "electron"))
(def Menu (.-Menu Electron))

(defn build-menu [template]
  (.buildFromTemplate Menu template))

(defn app-name []
  "Ploma")

(defn is-darwin? []
  (= js/process.platform "darwin"))

(defn toggle-dev-tools! [window]
  (if window (.toggleDevTools (.-webContents window))))

(defn reload! [window]
  (if window (.reload window)))

(defn create-main-menu []
  (let [template #js [
          #js {
            :label  (app-name),
            :submenu #js [
              #js { :role "about" }
              #js { :type "separator" }
              #js { :role "services" :submenu #js [] }
              #js { :type "separator" }
              #js { :role "hide" }
              #js { :role "hideothers" }
              #js { :role "unhide" }
              #js { :type "separator" }
              #js { :role "quit" }
            ]
          }
          #js {
            :label "File"
            :submenu #js [
              #js { :label "Open..." :click (fn [item focusedWindow] (js/console.log "User clicked 'Open...'")) }
            ]
          }
          #js {
            :label "Edit"
            :submenu #js [
              #js { :role "undo" }
              #js { :role "redo" }
              #js { :type "separator" }
              #js { :role "cut" }
              #js { :role "copy" }
              #js { :role "paste" }
              #js { :role "pasteandmatchstyle" }
              #js { :role "delete" }
              #js { :role "selectall" }
              ; Darwin only
              ; #js { :type "separator" }
              ; #js {
              ;   :label "Speech"
              ;   :submenu #js [
              ;     #js { :role "startspeaking" }
              ;     #js { :role "stopspeaking" }
              ;   ]
              ; }
            ]
          }
          #js {
            :label "View"
            :submenu #js [
              #js {
                :label "Reload"
                :accelerator "CmdOrCtrl+R"
                :click (fn [item focusedWindow] (reload! focusedWindow))
              }
              #js {
                :label "Toggle Developer Tools"
                :accelerator (if (is-darwin?) "Alt+Command+I" "Ctrl+Shift+I")
                :click (fn [item focusedWindow] (toggle-dev-tools! focusedWindow))
              }
              #js { :type "separator" }
              ; Non-Darwin
              ; #js { :role "resetzoom" }
              ; #js { :role "zoomin" }
              ; #js { :role "zoomout" }
              ; #js { :type "separator" }
              #js { :role "togglefullscreen" }
            ]
          }
          #js {
            :role "window"
            :submenu #js [
              ; Non-Darwin
              ; #js { :role "minimize" }
              ; #js { :role "close" }
              ; Darwin only
              #js {
                :label "Close"
                :accelerator "CmdOrCtrl+W"
                :role "close"
              }
              #js {
                :label "Minimize"
                :accelerator "CmdOrCtrl+M"
                :role "minimize"
              }
              #js {
                :label "Zoom"
                :role "zoom"
              }
              #js {
                :type "separator"
              }
              #js {
                :label "Bring All to Front"
                :role "front"
              }
            ]
          }
          #js {
            :role "help"
            :submenu #js [
              #js {
                :label "Learn More"
                :click (fn [] (.openExternal (.-shell Electron) "http://electron.atom.io"))
              }
            ]
          }
        ]]
    (build-menu template)
  ))

(defn set-app-menu! []
  (let [menu (create-main-menu)]
    (.setApplicationMenu Menu menu)))
