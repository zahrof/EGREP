import os
import random
import time

MIN_LOWER = 97
MAX_LOWER = 122
MIN_MAJ = 65
MAX_MAJ = 90
MAX_ITE = 6

def RegEx():
    s = ""
    r = random.random()
    if r < 0.3:
        s = s + chr(random.randint(MIN_MAJ,MAX_MAJ + 1))
    elif r < 0.6:
        sub = random.sample(range(MIN_LOWER, MAX_LOWER + 1), random.randint(2, 4))
        random.shuffle(sub)
        s = s + "(" + "|".join([chr(e) for e in sub]) + ")"

    for i in range(MAX_ITE):
        r = random.random()
        if r < 0.3:
            sub = random.sample(range(MIN_LOWER, MAX_LOWER + 1), random.randint(3, 7))
            random.shuffle(sub)
            c = sub.pop()
            s = s + "(" + "|".join([chr(e) for e in sub]) + ")*" +  chr(c)
        elif r < 0.3:
            sub = random.sample(range(MIN_LOWER, MAX_LOWER + 1), random.randint(2, 7))
            random.shuffle(sub)
            s = s + "(" + "|".join([chr(e) for e in sub]) + ")"
        else:
            sub = random.sample(range(MIN_LOWER, MAX_LOWER + 1), random.randint(1, 3))
            random.shuffle(sub)
            s = s + "".join([chr(e) for e in sub])


    return s

if __name__ == "__main__":
    #Création du fichier de test
    if True:
        file = open("RegEx.txt", 'w')
        for i in range(100):
            file.write(RegEx() + '\n')
        file.close()
        print("RegEx done.\njava...")

    #Ouverture du fichier Test
    lines = open("RegEx.txt", 'r').read().split('\n')[:-1]

    #Deplacement dans le src/java & Compilation du java
    os.chdir("../java/")
    os.popen("javac *.java")


    times = []
    for line in lines:
        t = time.time()
        os.popen("java Main \"" + line + "\" Babylone.txt")
        times.append(time.time() - t)

    print("java done.\nsave...")
    #Retour en src/python pour sauvegarde
    os.chdir("../python/")
    save = open("times_java.txt", 'w')
    for t in times:
        save.write(str(t) + '\n')
    save.close()

    lines = open("RegEx.txt", 'r').read().split('\n')[:-1]
    #Retour dans src/java pour Babylone.txt
    os.chdir("../java/")
    print("save done.\negrep...")
    times = []
    for line in lines:
        line = line.replace("(","[").replace(")","]").replace("|",",")
        t = time.time()
        os.popen("egrep \"" + line + "\" Babylone.txt")
        times.append(time.time() - t)

    print("egrep done.\nsave...")
    # Retour en src/python pour sauvegarde
    os.chdir("../python/")
    save = open("times_egrep.txt", 'w')
    for t in times:
        save.write(str(t) + '\n')
    save.close()
    print("Finshed!!")



