;ID 200846905

;====================== Part 1 - Warmup =============================
; No complexity calculations for Part1
; As Jonathan stated it's not needed in the Piazza.

(define (get-weight items)
  (define (summer sum items)
    (if (null? items)
        sum
        (summer (+ (caar items) sum) (cdr items))))
  (summer 0 items))

; Code if pretty much the same, just escaping the list error
; using another car instead of just cadr for items while adding.
(define (get-value items)
  (define (summer sum items)
    (if (null? items)
        sum
        (summer (+ (cadar items) sum) (cdr items))))
  (summer 0 items))

;====================== Part 2 - Not Optinal ========================

;B. Runtime of knapsack1
; 1. sort action - O(nlogn)
; 2. a recursion up to 'items' count, which means O(n) as all inner operations are O(1)
; O(nlogn) + O(n) = O(nlogn) TOTAL. 

;A. knapsack1 implementation
(define (knapsack1 items capacity)
  (define (inner items capacity inBag bagWeight)
    ;stop when we cant add more weigth to bag
    (if (> (+ bagWeight (caar items)) capacity)
        inBag
        (inner (cdr items) capacity (cons (car items) inBag) (+ bagWeight (caar items)))))
  ;sort as required
  (inner (sort items (lambda (i1 i2) (>= (/ (cadr i1) (car i1)) (/ (cadr i2) (car i2))))) capacity () 0))

;====================== Part 3 - Backtracking ======================

;B. Runtime of knapsack2
; All helper methods are O(1) - Including: valid-state, bigger-please-weight, bigger-please-val.
; For every item in the sack (items), i check with and without it.
; That means for n items we get O(2^n).
; O(1) + O(2^n) = O(2^n) TOTAL.

;A. knapsack2 implementation
(define (knapsack2 items capacity optimization-type)
  ; Optimization type if
  (if (eq? optimization-type 'weight)
      (solve-w items () capacity)
      (solve-v items () capacity)))

;Check that the state of the variables is valid, else false.
(define (valid-state item inBag capacity)
  (if (> (+ (get-weight inBag) (car item)) capacity)
      #f
      #t))

;Decide which have more items
(define (bigger-please-weight with without)
  (if (> (length without) (length with))
      without
      with))

;Decide which is bigger by value, and it they are equal return the smaller weighted list.
(define (bigger-please-val with without)
  (cond ((< (get-value without) (get-value with)) with)
        ((and (= (get-value without) (get-value with)) (< (get-weight without) (get-weight with))) without)
        ((and (= (get-value without) (get-value with)) (> (get-weight without) (get-weight with))) with)
        (else without)))

(define (solve-w items inBag capacity)
  (cond
    ;Finish Condition
    ((null? items) inBag)
    ;Skip to Next item condition
    ((not (valid-state (car items) inBag capacity)) (solve-w (cdr items) inBag capacity))
    ;Add item to solution Condition
    (else (let ((result-with-item (solve-w (cdr items) (cons (car items) inBag) capacity))
                (result-without-item (solve-w (cdr items) inBag capacity)))
            ; This is just to verify we can, and decide which solution direction to take.
            (bigger-please-weight result-with-item result-without-item)))))

(define (solve-v items inBag capacity)
  (cond
    ;Finish Condition
    ((null? items) inBag)
    ;Skip to Next item condition
    ((not (valid-state (car items) inBag capacity)) (solve-v (cdr items) inBag capacity))
    ;Add item to solution Condition
    (else (let ((result-with-item (solve-v (cdr items) (cons (car items) inBag) capacity))
                (result-without-item (solve-v (cdr items) inBag capacity)))
            ; This is just to verify we didnt get false, and decide which solution direction to take.
            (bigger-please-val result-with-item result-without-item)))))