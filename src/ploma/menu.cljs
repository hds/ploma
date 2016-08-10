(ns ploma.menu
    (:require [cljs.nodejs :as nodejs]))

(def Electron (nodejs/require "electron"))
(def Menu (.-Menu Electron))

; const menu = Menu.buildFromTemplate(template)
(defn build-menu [template]
  (.buildFromTemplate Menu template))

(defn create-main-menu []
  (let [template #js [
          ; const name = require('electron').remote.app.getName()
          #js {
            :label  "Ploma",
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
            ]
          }
          #js {
            :label "View"
            :submenu #js [
            #js { :label "Reload" :accelerator "CmdOrCtrl+R" :click (fn [item focusedWindow] (if focusedWindow (.reload focusedWindow))) }
            #js { :label "Toggle Developer Tools" :accelerator "Alt+Command+I" :click (fn [item focusedWindow] (js/console.log "Can't open developer tools right now."))}
            ]
          }
        ]]
    (build-menu template)
  ))

; Menu.setApplicationMenu(menu)
(defn set-app-menu! []
  (let [menu (create-main-menu)]
    (.setApplicationMenu Menu menu)))

; const {Menu} = require('electron')
;
; const template = [
;   {
;     label: 'Edit',
;     submenu: [
;       {
;         role: 'undo'
;       },
;       {
;         role: 'redo'
;       },
;       {
;         type: 'separator'
;       },
;       {
;         role: 'cut'
;       },
;       {
;         role: 'copy'
;       },
;       {
;         role: 'paste'
;       },
;       {
;         role: 'pasteandmatchstyle'
;       },
;       {
;         role: 'delete'
;       },
;       {
;         role: 'selectall'
;       }
;     ]
;   },
;   {
;     label: 'View',
;     submenu: [
;       {
;         label: 'Reload',
;         accelerator: 'CmdOrCtrl+R',
;         click (item, focusedWindow) {
;           if (focusedWindow) focusedWindow.reload()
;         }
;       },
;       {
;         label: 'Toggle Developer Tools',
;         accelerator: process.platform === 'darwin' ? 'Alt+Command+I' : 'Ctrl+Shift+I',
;         click (item, focusedWindow) {
;           if (focusedWindow) focusedWindow.webContents.toggleDevTools()
;         }
;       },
;       {
;         type: 'separator'
;       },
;       {
;         role: 'resetzoom'
;       },
;       {
;         role: 'zoomin'
;       },
;       {
;         role: 'zoomout'
;       },
;       {
;         type: 'separator'
;       },
;       {
;         role: 'togglefullscreen'
;       }
;     ]
;   },
;   {
;     role: 'window',
;     submenu: [
;       {
;         role: 'minimize'
;       },
;       {
;         role: 'close'
;       }
;     ]
;   },
;   {
;     role: 'help',
;     submenu: [
;       {
;         label: 'Learn More',
;         click () { require('electron').shell.openExternal('http://electron.atom.io') }
;       }
;     ]
;   }
; ]
;
; if (process.platform === 'darwin') {
;   const name = require('electron').remote.app.getName()
;   template.unshift({
;     label: name,
;     submenu: [
;       {
;         role: 'about'
;       },
;       {
;         type: 'separator'
;       },
;       {
;         role: 'services',
;         submenu: []
;       },
;       {
;         type: 'separator'
;       },
;       {
;         role: 'hide'
;       },
;       {
;         role: 'hideothers'
;       },
;       {
;         role: 'unhide'
;       },
;       {
;         type: 'separator'
;       },
;       {
;         role: 'quit'
;       }
;     ]
;   })
;   // Edit menu.
;   template[1].submenu.push(
;     {
;       type: 'separator'
;     },
;     {
;       label: 'Speech',
;       submenu: [
;         {
;           role: 'startspeaking'
;         },
;         {
;           role: 'stopspeaking'
;         }
;       ]
;     }
;   )
;   // Window menu.
;   template[3].submenu = [
;     {
;       label: 'Close',
;       accelerator: 'CmdOrCtrl+W',
;       role: 'close'
;     },
;     {
;       label: 'Minimize',
;       accelerator: 'CmdOrCtrl+M',
;       role: 'minimize'
;     },
;     {
;       label: 'Zoom',
;       role: 'zoom'
;     },
;     {
;       type: 'separator'
;     },
;     {
;       label: 'Bring All to Front',
;       role: 'front'
;     }
;   ]
; }
;
; const menu = Menu.buildFromTemplate(template)
; Menu.setApplicationMenu(menu)
