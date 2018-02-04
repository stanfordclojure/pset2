(ns pset2.rtc
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]))


(defn- delete-last [s]
  (string/join "" (drop-last s)))


(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))


(defn header [b]
  (->>
    (-> (re-find #"(.*)\{" b)
      last
      string/trim
      (string/split #":"))
    (map string/trim)
    (map #(string/split % #"\s"))))


(defn fields [b]
  (->>
    (-> (re-find #"\{(.*)\}" b)
      last
      string/trim
      (string/split #";"))
    (map string/trim)
    (map #(string/split % #"\s"))))


(defn process-raw-spec [raw-spec]
  (let [normalized (-> raw-spec
                     (string/replace #"[\n\t]" "")
                     (string/replace #"\s+" " ")
                     (delete-last))]
    {:header (header normalized)
     :fields (fields normalized)}))

(def url "https://www.w3.org/TR/webrtc/")
(def w3c-html (fetch-url url))
(def idl-text (map html/text (html/select w3c-html [:.def.idl :> html/any-node])))


(process-raw-spec (println (nth idl-text 0)))


(comment
  "Example IDL and corresponding spec:"


  "dictionary RTCConfiguration {
      sequence<RTCIceServer>   iceServers;
      RTCIceTransportPolicy    iceTransportPolicy = \"all\";
      RTCBundlePolicy          bundlePolicy = \"balanced\";
      RTCRtcpMuxPolicy         rtcpMuxPolicy = \"require\";
      DOMString                peerIdentity;
      sequence<RTCCertificate> certificates;
      [EnforceRange]
      octet                    iceCandidatePoolSize = 0;
  };"

  ;; we don't understand the type of RTCIceServer from this example
  (s/def ::RTCIceServer ???)
  ;; we can only populate a subset of this enum using this single example
  ;; it's defined later, and we see it's an enum containing #{"all" "relay"}
  (s/def ::RTCIceTransportPolicy #{"all"})
  (s/def ::RTCBundlePolicy #{"balanced"})
  (s/def ::RTCRtcpMuxPolicy #{"require"})
  (s/def ::DOMString string?)
  (s/def ::RTCCertificate ???)
  (s/def ::octet ???)

  (s/def ::iceServers (s/* ::RTCIceServer))
  (s/def ::iceTransportPolicy ::iceTransportPolicy)
  (s/def ::bundlePolicy ::bundlePolicy)
  (s/def ::rtcpMuxPolicy ::RTCRtcpMuxPolicy)
  (s/def ::peerIdentity ::DOMString)
  (s/def ::certificates (s/* ::RTCCertificate))
  (s/def ::iceCandidatePoolSize ::octet)

  (def defaults {::iceTransportPolicy "all"
                 ::bundlePolicy "balanced"
                 ::rtcpMuxPolicy "require"
                 ::iceCandidatePoolSize 0})

  (s/def ::RTCConfiguration
    (s/keys :opt [::iceServers
                  ::iceTransportPolicy
                  ::bundlePolicy
                  ::rtcpMuxPolicy
                  ::peerIdentity
                  ::certificates
                  ::iceCandidatePoolSize]))

  )
