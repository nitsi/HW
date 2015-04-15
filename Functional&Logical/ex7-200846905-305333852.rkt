; Ex7
; 1st ID: 200846905
; 2nd ID: 305333852


; Global interpreter constants
(define CONTEXT_TYPE 'static) ; can be either 'static or 'dynamic
(define PRINT_MACRO_EXPANSION #f)

; Bonus constant - change to #t if you implement the bonus. Keep on #f otherwise.
(define SWITCH_IP_ENABLED #f)

; ********************************************************************************************
; * Do not change anything in the code below, until where marked 'Your code should be here'. *
; * You may only change value of user-definitions if you do part 7.                          *
; ********************************************************************************************

; Special keywords - special forms that are implemented in the interpreter
(define special-keywords '(() #t #f lambda nlambda macro if eval apply))

; Primitive functions - functions that are used as primitives from the Dr. Racket interpreter
(define primitive-functions '(+ - * / < > <= >= <> = eq? equal? null? pair? cons car cdr))

; System context - contains default system functions (non-primitive) and constants - Can add more here
(define system-definitions '((pi 3.14159265358979)
                             (list (lambda x x))
                             (quote (nlambda (x) x))
                             (caar (macro (p) (list 'car (list 'car p))))
                             (cadr (macro (p) (list 'car (list 'cdr p))))
                             (cadar (macro (p) (list 'car (list 'cdr (list 'car p)))))
                             (cond-make-conds (lambda (conds)
                                                (if (null? conds)
                                                    ()
                                                    (if (eq? 'else (caar conds))
                                                        (cadar conds)
                                                        (list 'if (caar conds) (cadar conds)
                                                              (cond-make-conds (cdr conds)))))))
                             (cond (macro conds (cond-make-conds conds)))
                             (map (lambda (pred lst)
                                    (if (null? lst) ()
                                        (cons (pred (car lst)) (map pred (cdr lst))))))
                             (append (lambda (lst1 lst2)
                                       (if (null? lst1) lst2
                                           (cons (car lst1) (append (cdr lst1) lst2)))))
                             (let (macro (defs body) 
                                         (append (list (list 'lambda (map car defs) body))
                                                 (map cadr defs))))
                             ))

; User context - contains user functions (non-primitive) and constants - Can add more here
(define user-definitions '((first (macro (lst) (list 'car lst)))
                           (second (macro (lst) (list 'car (list 'cdr lst))))
                           (third (macro (lst) (list 'car (list 'cdr (list 'cdr lst)))))
                           (fourth (macro (lst) (list 'car (list 'cdr (list 'cdr (list 'cdr lst))))))
                           ; ***********************
                           ; * Add bonus code here *
                           ; ***********************
                           ))

; Makes a context out of a given list of definitions
(define (make-context dict)
  (if (null? dict) ()
      (dict-put (caar dict) (evaluate (cadar dict) ()) (make-context (cdr dict)))))

; Runs user code with an empty initial context
(define (run-code expr)
  (evaluate expr ()))

; Shows a prompt to the user to enter his code to run
(define (show-prompt-loop)
  (display "Enter an expression (type 'exit' to stop):")
  (newline)
  (let ((exp (read)))
    (if (not (eq? exp 'exit))
        (let ((result (run-code exp)))
          (if (not (eq? result (void)))
              (begin
                (display result)
                (newline)))
          (show-prompt-loop)))))

; Dictionary management (from class)
(define (dict-put key value ctx)
  (cons (list key value) ctx))

(define (dict-put-many entries ctx)
  (append entries ctx))

(define (dict-get key ctx)
  (let ((res (assoc key ctx)))
    (if res (cadr res) '_value_not_in_dict)))

; ***************************************************************************************
; ********************************* Add your code here! *********************************
; ***************************************************************************************

;Part 1
(define (eval-args args ctx)
  (map (lambda (arg) (evaluate arg ctx)) args))

;Part 2
(define (bind params args)
  (define (binder params args nlist)
    (cond ((null? params) nlist)
          ((not (pair? params)) (binder () () (append nlist (list (list params args)))))
          (else (binder (cdr params) (cdr args) (append nlist (list (list (car params) (car args))))))))
  (binder params args ()))

;Part 3
(define (eval-symbol sym ctx)
  (cond ((member sym special-keywords) sym)
        ((member sym primitive-functions) (list '_primitive (eval sym)))
        ((not (eq? (dict-get sym ctx) '_value_not_in_dict)) (dict-get sym ctx))
        ((not (eq? (dict-get sym user-context) '_value_not_in_dict)) (dict-get sym user-context))
        ((not (eq? (dict-get sym system-context) '_value_not_in_dict)) (dict-get sym system-context))
        (else (error "reference to undefined identifier"))))

;Part 4
(define (eval-if condition if-true if-false ctx)
  (if (evaluate condition ctx)
      (evaluate if-true ctx)
      (evaluate if-false ctx)))

;Part 5
(define (exec-func func args ctx)
  (if (eq? (car func) '_primitive)
      (apply (cadr func) (eval-args args ctx))
      (exec-user-func func args ctx)))

(define (exec-apply func args-list ctx) ; << Not really understood
  (evaluate (list func (eval-args args ctx)) ctx))

(define (exec-user-func func args ctx)
  ; variable definision as same as in Ex7 page.
  (let ((type (first func))
        (params (second func))
        (body (third func))
        (binded (if (eq? '_user_lambda (first func))
                    (bind (second func) (eval-args args ctx))
                    (bind (second func) args)))
        (ctx-creation (if (not (eq? CONTEXT_TYPE 'static)) ;ctx-creation = func-eval-ctx
                          ctx
                          (fourth func))))
    ; Finished definitions, added binded to make the next part of code
    ; more readable.
    (cond ((eq? '_user_lambda type) 
           (evaluate body (dict-put-many binded ctx-creation)))
          ((eq? '_user_nlambda type)
           (evaluate body (dict-put-many binded ctx-creation)))
          ((eq? '_user_macro type)
           (let ((expansion (evaluate body (dict-put-many (bind params args) ctx-creation))))
             (if PRINT_MACRO_EXPANSION
                 (begin
                   (println "Macro: ")
                   (println body)
                   (println "Result:")
                   (println expansion)))
             (evaluate expansion ctx)))
          (else (error "please validate definition of user type: " type))
          )))

;Part 6
(define (evaluate exp ctx)
  ; Check of exp -> symbol or not 
  (cond ((symbol? exp) (eval-symbol exp ctx))
        ((not (pair? exp)) exp)
        (else (let* ((func (evaluate (car exp) ctx))
                    (args (cdr exp)))
                    ; I didnt add variables for (car args) or (cadr args) as i might recieve null
                    ; and it will cause contract violation or other bad exceptions.
                    
                ; Finished setting vars, continue relevant check of func
                (cond        
                  ((eq? func 'lambda) `(_user_lambda ,(car args) ,(cadr args) ,ctx))
                  ((eq? func 'nlambda) `(_user_nlambda ,(car args) ,(cadr args) ,ctx))
                  ((eq? func 'macro) `(_user_macro ,(car args) ,(cadr args) ,ctx))
                  ((eq? func 'if) (eval-if (car args) (cadr args) (caddr args) ctx))
                  ((eq? func 'eval) (evaluate (evaluate (car args) ctx) ctx))
                  ((eq? func 'apply) (exec-apply (car args) (cadr args) ctx))
                  (else (exec-func func args ctx)))))))


; ***************************************************************************************
; *           The following lines should appear at the end, BELOW your code!            *
; *                            Do NOT change the code below                             *
; ***************************************************************************************

; Initially create system context
(define system-context (make-context system-definitions))

; Initially create user context
(define user-context (make-context user-definitions))