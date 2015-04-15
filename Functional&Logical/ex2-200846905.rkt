; ID 200846905
; Exercise 2


;====================== Question 1 ======================
;A
(define cars 
  (lambda (lists)
    (if (not (null? (cdr lists)))
        (cons (car (car lists)) (cars (cdr lists)))
        (cons (car (car lists)) (cdr lists)))))

;B
(define cdrs
  (lambda (lists)
    (if (not (null? (cdr lists)))
        (cons (cdr (car lists)) (cdrs (cdr lists)))
        (cons (cdr (car lists)) ()))))

;====================== Question 2 ======================
; The split function is reusable, but i did as asked in HW and copied the
; code from A to B

;A - quick-sort
(define quick-sort
  (lambda (lst)
    (define (split lst pivot)
  (define (helper pivot lst l1 l2)
    (if (null? lst)
        (list l1 l2)
        ;In case its bigger then pivot, send to right list, else to the left
        (cond ((> (car lst) pivot)
               (helper pivot (cdr lst) l1 (cons (car lst) l2)))
              (else (helper pivot (cdr lst) (cons (car lst) l1) l2))
              )))
  (helper pivot lst () ()))
    (define (pivot lst) (car lst))
    (if (and (not (null? lst)) (not (null? (cdr lst))))
        (let* ((splittedList (split lst (pivot lst))))
          (append
           (quick-sort (first splittedList))
           (quick-sort (second splittedList))))
        lst)))

;B - smarter quick-sort
(define quick-sort-with-pivot
  (lambda (lst)
    (define (split lst pivot)
  (define (helper pivot lst l1 l2)
    (if (null? lst)
        (list l1 l2)
        ;In case its bigger then pivot, send to right list, else to the left
        (cond ((> (car lst) pivot)
               (helper pivot (cdr lst) l1 (cons (car lst) l2)))
              (else (helper pivot (cdr lst) (cons (car lst) l1) l2))
              )))
  (helper pivot lst () ()))
    ;Pivot - length divided by 2, i always take care of odd/even lists with floor.
    (define (pivot lst) (list-ref lst (floor (/ (length lst) 2))))
    (if (and (not (null? lst)) (not (null? (cdr lst))))
        (let* ((splittedList (split lst (pivot lst))))
          (append
           (quick-sort (first splittedList))
           (quick-sort (second splittedList))))
        lst)))

;====================== Question 3 ======================
;A - Counter of "1" in bits of a number
(define numOfBitsOn
  (lambda (number)
    (define (counter bits num)
      (if (eq? num 0)
          bits
          ;remembering carry as we count num of bits with modulo
          (counter (+ (modulo num 2) bits) (floor (/ num 2)))))
    (counter 0 number)))

;B - Square root, good enough by delta
(define findSqrt
  (lambda (n delta)
    (define (finder guess min max n delta)
      (cond ((< (abs (- (* guess guess) n)) delta) guess)
            ((< (- (* guess guess) n) 0)
             (finder (/ (+ guess max) 2) guess max n delta))
            (else
             (finder (/ (+ guess min) 2) min guess n delta))))
    (finder (/ n 4) 0 (/ n 2) n delta)))

;C - cars by tail recursion
(define cars-tail
  (lambda (lists)
    (define (recCars newList lists)
      (if (not (null? (cdr lists)))
          (recCars (append newList (list (car (car lists))))
                   (cdr lists))
          (append newList (list (caar lists)))))
    (recCars () lists)))

;D - cdrs by tail recursion
(define cdrs-tail
  (lambda (lists)
    (define (recCdrs newList lists)
      (if (not (null? (cdr lists)))
          (recCdrs (append newList (list (cdr (car lists))))
                   (cdr lists))
          (append newList (list (cdr (car lists))))))
    (recCdrs () lists)))

