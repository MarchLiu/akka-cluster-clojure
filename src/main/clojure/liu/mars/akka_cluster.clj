(ns liu.mars.akka-cluster
  (:require [liu.mars.actor :refer [!]])
  (:import (akka.actor AbstractActor ActorSystem ActorRef)
           (akka.cluster.pubsub DistributedPubSub DistributedPubSubMediator$Subscribe
                                DistributedPubSubMediator$Publish
                                DistributedPubSubMediator$Put
                                DistributedPubSubMediator$Send
                                DistributedPubSubMediator$SendToAll
                                DistributedPubSubMediator$Unsubscribe
                                DistributedPubSubMediator$Remove)
           (akka.cluster.client ClusterClientSettings ClusterClient SubscribeContactPoints SubscribeClusterClients)
           (java.util Set)))

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
  ([path message self local-affinity]
   (let [m (mediator self)]
     (send m path message self local-affinity))))

(defn send-all
  ([m path message self all-but-self]
   (! m (DistributedPubSubMediator$SendToAll. path message all-but-self) self))
  ([path message self all-but-self]
   (let [m (mediator self)]
     (send m path message self all-but-self))))

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
       (#(.actorOf system % name)))))

(defn subscribe-contact-points [^ActorRef client ^ActorRef to]
  (! client (SubscribeContactPoints/getInstance) to))

(defn subscribe-cluster-clients [^ActorRef client ^ActorRef to]
  (! client (SubscribeClusterClients/getInstance) to))