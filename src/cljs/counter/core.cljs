(ns counter.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]))

;; -------------------------
;; Views

(defn increment-count [ratom]
  (swap! ratom update-in [:count] inc))

(defn decrement-count [ratom]
  (swap! ratom update-in [:count] dec))

(defonce counter1-state (reagent/atom {:count 0}))

(defn home-page []
  [:div "Current count: " (@counter1-state :count)
   [:div
    [:button {:on-click #(increment-count counter1-state)}
     "Increment"]
    [:button {:on-click #(decrement-count counter1-state)}
    "Decrement"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
