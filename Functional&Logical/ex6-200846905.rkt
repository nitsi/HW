; ID 200846905
; This is Ex6 (including Ex5 at start)

; Part 1 - Ex 5
;====================== Part 1 - A ======================
; My dictionary implementation:
; The dictionary is a list of lists.
; Every item in the dictonary is a list by itself.
; Key as first item (car),
; and all the other items in this list (cdr) are the values.

;====================== Part 1 - B ======================
(define (multidict-get key dict)
  (define (searcher key dict)
    (if (null? dict)
        #f
        (if (eq? key (caar dict))
            (cdar dict)
            (searcher key (cdr dict)))))
  (searcher key dict))

;====================== Part 1 - C ======================
(define (multidict-remove key dict)
  (define (remover key part dict)
    (if (null? dict)
        part
        (if (eq? key (caar dict))
            (remover key part (cdr dict))
            (remover key (append part (list (car dict))) (cdr dict)))))
  (remover key () dict))

;====================== Part 1 - D ======================
(define (multidict-put key value dict)
  ; iterate and add it to the relevant key
  (define (adder key value part dict)
    (if (null? dict)
        part
        (if (eq? key (caar dict))
            (adder key value (append part (list (list* (caar dict) value (cdar dict)))) (cdr dict))
            (adder key value (append part (list (car dict))) (cdr dict)))))
  ;if the key is part of the dictonary, find it & add it
  ;else, add it directly to the end
  (if (multidict-get key dict)
      (adder key value () dict)
      (append dict (list (list key value)))))
;====================== Ex5 END ======================


; Part 2 - Ex6
; ====================================================
; Our Basic building blocks - as defined in class
(defmacro stream-cons (value next-expr)
  `(cons ,value (lambda () ,next-expr)))

(define stream-car car)

(define (stream-cdr stream)
  (if (procedure? (cdr stream))
      ((cdr stream))
      (cdr stream)))

;====================== Part 2 ======================
;A
(define (generate-even-stream)
  (define (evener i)
    (stream-cons i (evener (+ i 2))))
  (evener 2))

;B
(define (generate-fibo)
  (define (fib i1 i2)
    (stream-cons i2 (fib i2 (+ i1 i2))))
  (fib 0 1))

;====================== Part 3 ======================
;A
(define (list-to-stream lst)
  (if (null? (cdr lst))
      (stream-cons (car lst) ())
      (stream-cons (car lst) (list-to-stream (cdr lst)))))

;B
(define (list-to-infinite-stream lst)
  (define (infi olist lst)
    (if (null? (cdr olist))
        (stream-cons (car olist) (infi lst lst))
        (stream-cons (car olist) (infi (cdr olist) lst))))
  (infi lst lst))

;====================== Part 4 ======================
(define (stream-comp action baseStream . conditions)
  ;This method checks that the current item from the list
  ;returns True for all the conditions
  (define (validate item conditions)
    (if (null? conditions)
        #t
        (if ((car conditions) item)
            (validate item (cdr conditions))
            #f)))
  ;Create the new stream and apply the action when needed
  (define (streamer action baseStream conditions)
    (if (validate (stream-car baseStream) conditions)
        (stream-cons (action (stream-car baseStream)) (streamer action (stream-cdr baseStream) conditions)) 
        (streamer action (stream-cdr baseStream) conditions)))
  (streamer action baseStream conditions))