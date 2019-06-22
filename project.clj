(defproject liu.mars/akka-cluster-clojure "0.1.2"
  :description "A Clojure Library for akka cluster"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/clojure"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.typesafe.akka/akka-actor_2.12 "2.5.23"]
                 [com.typesafe.akka/akka-cluster-tools_2.12 "2.5.23"]
                 [liu.mars/akka-clojure "0.2.1"]]
  :repl-options {:init-ns liu.mars.akka-cluster}
  :aot :all)
