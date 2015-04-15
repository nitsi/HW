; ID: 200846905
;
; A. I:   ((300 + 11) + (8 * 9))
;    II:  ((1 + 2) * 8) + (33 / 11)
;    III: (((((((7 * 6) * 8) * 5) * 4) * 3) * 2) * 1)
;
; B. I:   (+ 5 (- (/ 8 2) (* 8 9)))
;    II:  (* (+ (+ 8 4) 5) (/ (+ 9 7) (* (* 8 7) 6)))
;    III: (- 2 (/ (+ 4 (* 8 9)) 4)
;

;Part2 - Qt. A
(define (positiveOdd x) 
  (if (and (> x 0) (> (modulo x 2) 0))
      'yes
      'no))

;Part2 - Qt. B
(define (circle-area r)
  (* (* r r) pi))

;Part3
(define (someSequence n)
  (if (= n 1)
      1
      (+ (someSequence(- n 1)) (expt (* n 2) n))))

;Part4
; fibo is the main function, fiboRecc is the actually recursion
(define (fibo n)
  (display 1)
  (newline)
  (cond ((= n 2) (display 1))
        ((> n 2) (fiboRecc 1 2 (- n 3)))))

; An = N1 + N2, where N1 is the smaller number in the series.
(define (fiboRecc n1 n2 counter)
  (display n1)
  (newline)
  (if (< 0 counter)
      (fiboRecc n2 (+ n1 n2) (- counter 1))
      (display n2)))
      
  