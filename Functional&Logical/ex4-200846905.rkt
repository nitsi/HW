; ID 200846905
; Exercise 4
;====================== Question 1 ======================
;A
(defmacro circle-area (r)
  `(* pi ,r ,r))

;B
(defmacro min (x y)
  `(if (> ,y ,x)
       ,x
       ,y))

;====================== Question 2 ======================
;A
(define (test-ip ip prefix)
  (if (or (null? ip) (null? prefix))
      #t
      (if (= (car ip) (car prefix))
          (test-ip (cdr ip) (cdr prefix))
          #f)))

;B
(define (make-ip-filter prefix)
  (define (checkRange ipList prefix match)
    (if (not (null? ipList))
        (if (test-ip (car ipList) prefix)   
            (checkRange (cdr ipList) prefix (append match (list (car ipList))))
            (checkRange (cdr ipList) prefix match))
        match))
  (lambda (ipList) (checkRange ipList prefix ())))

;C - what do you think about 
;    the name DNS for the function ? :)
(defmacro switch-ip (ip . cases)
  (define (dns case)
    (if (eq? (car case) 'default)
        `(else ,(cadr case))
        `((test-ip val ',(car case)) ,(cadr case))))
  `(let ((val ,ip))
     (cond ,@(map dns cases))))

;====================== Question 3 ======================
;A - XOR - an odd number of #t expressions
(defmacro xor (x . y)
  (define (valueIt terms reps)
    (if (null? terms)
        (if (not (odd? reps))
            #f
            #t)
        `(if ,(car terms)
             ,(valueIt (cdr terms) (+ 1 reps))
             ,(valueIt (cdr terms) reps)
             )))
  (valueIt (cons x y) 0))

;B - NAND - If all #t -> #f | if there is a #f -> #t
(defmacro nand (x . y)
  (define (falser terms)
    (if (null? terms)
        #f
        `(if ,(car terms)
             ,(falser (cdr terms))
             #t)
        ))
  (falser (cons x y)))