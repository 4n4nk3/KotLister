# KotLister
A simple wordlist generator and mangler written in Kotlin as a first exercise to try out this language.

### Supported permutations:
 - [x] Capital
 - [x] Upper
 - [x] 1337
 - [x] Append
 - [x] Prepend

### Additional functions:
 - [x] Each generated password doesn't contain same word twice.
 
 ### Preview:
 
```
usage: [-h] [-i INPUT] [-p PERM] [--min MIN] [--max MAX] [--append APPEND]
       [--prepend PREPEND] [-l] [-u] [-c]

optional arguments:
  -h, --help          show this help message and exit

  -i INPUT,           input file path
  --input INPUT

  -p PERM,            max number of words to be combined on the same line
  --perm PERM

  --min MIN           minimum generated password length

  --max MAX           maximum generated password length

  --append APPEND     append chosen word

  --prepend PREPEND   prepend chosen word

  -l, --leet          enable leet mutagen

  -u, --upper         enable uppercase mutagen

  -c, --capitalize    enable capitalize mutagen
```


**_This project is for educational purposes only. Don't use it for illegal activities. I don't support nor condone illegal or unethical actions and I can't be held responsible for possible misuse of this software._**