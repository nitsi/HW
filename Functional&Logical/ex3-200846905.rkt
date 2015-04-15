; Exercise 3
; ID: 200846905

;====================== Part 1 - Quick-Sort ======================
(define bonus-included #t)

(define (quick-sort pred lst)
  (if (and (not (null? lst)) (not (null? (cdr lst))))
      (let* ((splittedList (split lst pred)))
        (append
         (quick-sort pred (first splittedList))
         (quick-sort pred (second splittedList))))
      lst))

(define (pivoter lst) (car lst))

(define (split lst pred)
  (if (null? lst)
      ()
      (list (append (filter (lambda (x) (pred x (car lst))) lst) (list (car lst)))
            (filter (lambda (x) (not (pred x (car lst)))) (cdr lst)))))

;====================== Part 2 - Factories ======================
;Qt 1
(define (do2add lst)
  (define (plus xlist newlist)
    (if (not (null? xlist))
        (plus (cddr xlist) (append newlist (list (+ (car xlist) (cadr xlist)))))
        newlist))
  (plus lst ()))

;Qt 2
(define (do2F F lst)
  (define (newFunc predicate xlist newlist)
    (if (not (null? xlist))
        (newFunc predicate (cddr xlist) (append newlist (list (predicate (car xlist) (cadr xlist)))))
        newlist))
  (newFunc F lst ()))

;Qt 3
(define (makeDo2F F)
  (lambda (newlist) (do2F F newlist)))

;Qt 4
(define do2addFactory
  (makeDo2F +))

(define do2mult
  (makeDo2F *))

(define do2eq?
  (makeDo2F eq?))

(define do2eq1st
  (makeDo2F (lambda (i1 i2) (eq? (car i1) (car i2)))))

;====================== Part 3 - Multiple Arguments ======================
;Qt 1
(define (do2FM F x y z)
  (define (newFunc predicate xlist newlist)
    (if (not (null? xlist))
        (newFunc predicate (cddr xlist) (append newlist (list (predicate (car xlist) (cadr xlist)))))
        newlist))
  (newFunc F z (list (F x y))))

(define (makeDo2FM F)
  (lambda (x y . z) (do2FM F x y z)))