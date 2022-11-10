(ns lab2.core
  (:require [clojure.core :as core]
            [clojure.string]
            [clojure.java.io :as jio])
  (:require [clojure.core.async :as a]))

(def test-file "src/lab2/test-file.txt")

 (defn char-seq
   [^java.io.Reader rdr]
   (let [chr (.read rdr)]
     (if (>= chr 0)
       (cons chr (lazy-seq (char-seq rdr))))))

(defn file-to-char-channel
  "Returns channel, formed from file. Returned channel is closed."
  [file-path]
  (let [channel (a/chan)]
    (with-open [reader (jio/reader file-path)]
      (let [char_seq (char-seq reader)]
        (doseq [character char_seq]
          (a/put! channel (char character))
          ;; (print (char character) " ") 
          )
        ;; (println)
        ))
    (a/close! channel)
    channel))

(def char-channel (file-to-char-channel test-file))

(defn char-channel-to-string-channel
  "Returns channel, formed from channel that contains characters. Returned channel is closed."
  [char-channel]
  (let [string-channel (a/chan)]
    (let [string-from-channel (apply str (a/<!! (a/into [] char-channel)))]
      (doseq [substring (clojure.string/split string-from-channel #" ")]
        (a/put! string-channel substring)))
    (a/close! string-channel)
    string-channel)
    )

(def test-channel (char-channel-to-string-channel char-channel))
(doseq [x (range 2)]
  (println (a/<!! test-channel)))
