import matplotlib.pyplot as plt

def size(s):
    c = 0
    for e in s:
        if e in "|":
            c = c + 1
    return c



if __name__ == "__main__":

    if True:
        egrep = [float(t) for t in open("times_egrep.txt", 'r').read().split('\n')[:-1]]
        java = [float(t) for t in open("times_java.txt", 'r').read().split('\n')[:-1]]

        plt.scatter(egrep, java)

        plt.xlabel("egrep")
        plt.ylabel("java")

        echelle = 0.1
        plt.xlim(0, 0.1)
        plt.ylim(0, echelle)

        plt.plot([0, echelle], [0, echelle], color="black", label="f(x) = x")
        plt.xlabel("temps d'execution egrep (en s)")
        plt.ylabel("temps d'execution du clone (en s)")
        plt.legend()
        plt.title("Compairaison d'egrep et du clone sur un même RegEx")
        plt.savefig("egrep_java.png")
        plt.show()

    if False:
        RegEx = open("RegEx.txt", 'r').read().split('\n')[:-1]
        times = [float(t) for t in open("times_java.txt", 'r').read().split('\n')[:-1]]
        plt.scatter([size(e) for e in RegEx], times)
        plt.savefig("java_size.png")
        plt.show()








