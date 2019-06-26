(ns liu.mars.cluster-client
  (:require [liu.mars.actor :refer [!]])
  (:import (akka.cluster.client ClusterClientSettings ClusterClient SubscribeContactPoints SubscribeClusterClients ClusterClientReceptionist ClusterClient$Send ClusterClient$SendToAll)
           (akka.actor ActorSystem ActorRef)
           (java.util Set)))

(defn client-with
  ([^ActorSystem system ^Set contacts ^String name]
   (-> system
       (ClusterClientSettings/create)
       (.withInitialContacts contacts)
       (ClusterClient/props)
       (#(.actorOf system % name))))
  ([^ActorSystem system ^Set contacts]
   (-> system
       (ClusterClientSettings/create)
       (.withInitialContacts contacts)
       (ClusterClient/props)
       (#(.actorOf system %)))))

(defn subscribe-contact-points [^ActorRef client ^ActorRef to]
  (! client (SubscribeContactPoints/getInstance) to))

(defn subscribe-cluster-clients [^ActorRef client ^ActorRef to]
  (! client (SubscribeClusterClients/getInstance) to))

(defn register-service [system props name]
  (-> system
      ClusterClientReceptionist/get
      (.registerService (.actorOf system props name))))

(defn send [client path message self local-affinity]
  (! client (ClusterClient$Send. path message local-affinity) self))

(defn send-all [client path message self]
  (! client (ClusterClient$SendToAll. path message) self))