(ns liu.mars.cluster
  (:require [liu.mars.actor :refer [!]])
  (:import (akka.actor AbstractActor)
           (akka.cluster.pubsub DistributedPubSub DistributedPubSubMediator$Subscribe
                                DistributedPubSubMediator$Publish
                                DistributedPubSubMediator$Put
                                DistributedPubSubMediator$Send
                                DistributedPubSubMediator$SendToAll
                                DistributedPubSubMediator$Unsubscribe
                                DistributedPubSubMediator$Remove)))

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
   (let [m (mediator self)]
     (subscribe m topic self)
     (-> self
         .getContext
         (.stop m)))))

(defn unsubscribe
  ([m topic self]
   (! m (DistributedPubSubMediator$Unsubscribe. topic self) self))
  ([topic self]
   (let [m (mediator self)]
     (unsubscribe m topic self))))

(defn publish
  ([m topic message self]
   (! m (DistributedPubSubMediator$Publish. topic message) self))
  ([topic message self]
   (let [m (mediator self)]
     (publish m topic message self))))

(defn put
  ([m self]
   (! m (DistributedPubSubMediator$Put. self) self))
  ([self]
   (let [m (mediator self)]
     (put m self))))

(defn remove
  ([m self]
   (! m (DistributedPubSubMediator$Remove. self) self))
  ([self]
   (let [m (mediator self)]
     (remove m self))))

(defn send
  ([m path message self local-affinity]
   (! m (DistributedPubSubMediator$Send. path message local-affinity) self))
  ([path message this local-affinity]
   (let [m (mediator this)]
     (send m path message (.getSelf this) local-affinity))))

(defn send-all
  ([m path message self all-but-self]
   (! m (DistributedPubSubMediator$SendToAll. path message all-but-self) self))
  ([path message this all-but-self]
   (let [m (mediator this)]
     (send m path message (.getSelf this) all-but-self))))
