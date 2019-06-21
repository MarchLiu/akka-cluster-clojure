(ns liu.mars.akka-cluster
  (:require [liu.mars.actor :refer [!]])
  (:import (akka.actor AbstractActor)
           (akka.cluster.pubsub DistributedPubSub DistributedPubSubMediator$Subscribe DistributedPubSubMediator$Publish DistributedPubSubMediator DistributedPubSubMediator$Put DistributedPubSubMediator$Send DistributedPubSubMediator$SendToAll DistributedPubSubMediator$Unsubscribe DistributedPubSubMediator$Remove)))

(defn mediator [^AbstractActor actor]
  (-> actor
      .getContext
      .system
      (DistributedPubSub/get)
      .mediator))

(defn subscribe
  ([m topic self]
   (! m (DistributedPubSubMediator$Subscribe. topic self) self))
  ([topic self]
   (subscribe (mediator self) topic self)))

(defn unsubscribe
  ([m topic self]
   (! m (DistributedPubSubMediator$Unsubscribe. topic self) self))
  ([topic self]
   (unsubscribe (mediator self) topic self)))

(defn publish
  ([m topic message self]
   (! m (DistributedPubSubMediator$Publish. topic message) self))
  ([topic message self]
   (publish (mediator self) topic message self)))

(defn put
  ([m self]
   (! m (DistributedPubSubMediator$Put. self) self))
  ([self]
   (put (mediator self) self)))

(defn remove
  ([m self]
   (! m (DistributedPubSubMediator$Remove. self) self))
  ([self]
   (remove (mediator self) self)))

(defn send
  ([m path message self local-affinity]
   (! m (DistributedPubSubMediator$Send. path message local-affinity) self))
  ([path message self local-affinity]
   (send (mediator self) path message self local-affinity)))

(defn send-all
  ([m path message self all-but-self]
   (! m (DistributedPubSubMediator$SendToAll. path message all-but-self) self))
  ([path message self all-but-self]
   (send (mediator self) path message self all-but-self)))