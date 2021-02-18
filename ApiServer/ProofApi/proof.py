import sys
import json
import random
import math
import numpy
import hashlib
import datetime
from random import SystemRandom
from random import randrange

Spra = 256
SRpra = 1024  # secure parameter

par = {
    "N": 1730319361222680428228290691072800465681558218004766326513192206944185807019297659343329143903818492158850581211231765286181019363240633008554131425173323,
    "g": 2,
    "h": 1531199841189175461873773360868120469608134006525219501102095337818440502129328665593613507872551613209395420879786426238875863915627205422651985208980175,
}

# *************** Committed Number is a Square Proof ***************


def SQR(a, r1, g, h, E, N):
    while True:
        r2 = random.getrandbits(SRpra)
        r3 = r1 - r2
        if (r3 > 0):
            break
    F = (pow(g, a, N)*pow(h, r2, N)) % N
    EP = (pow(F, a, N)*pow(h, r3, N)) % N
    el = EL(a, r2, r3, g, h, F, h, F, EP, N)
    el.extend([F, EP])
    return el


def VSQ(sq, g, h, E, N):  # sq[0](H,X,X1,X2) sq()
    A1 = commit(sq[0][1], sq[0][2], g, h, N)
    A2 = commit(sq[0][1], sq[0][3], sq[1], h, N)

    C1 = (A1 * modinv(pow(sq[1], sq[0][0], N), N)) % N
    C2 = (A2 * modinv(pow(sq[2], sq[0][0], N), N)) % N

    if sq[0][0] != int(hashlib.sha256((str(C1)+str(C2)).encode()).hexdigest(), 16):
        return False

    return True

# *************** Committed Number is a Square Proof ***************


# **************** Two Commitments Hide the Same Secret Proof  ****************

def EL(m, r1, r2, g1, h1, g2, h2, c1, c2, N):
    u = random.getrandbits(SRpra)
    v1 = random.getrandbits(SRpra)
    v2 = random.getrandbits(SRpra)
    C1 = commit(u, v1, g1, h1, N)
    C2 = commit(u, v2, g2, h2, N)
    H = int(hashlib.sha256((str(C1)+str(C2)).encode()).hexdigest(), 16)
    X = u + H * m
    X1 = v1 + H * r1
    X2 = v2 + H * r2

    return [H, X, X1, X2]


def VEL(el, g1, h1, g2, h2, c1, c2, N):  # el =(H,X,X1,X2)
    A1 = commit(el[1], el[2], g1, h1, N)
    A2 = commit(el[1], el[3], g2, h2, N)

    C1 = (A1 * modinv(pow(c1, el[0], N), N)) % N
    C2 = (A2 * modinv(pow(c2, el[0], N), N)) % N

    if el[0] != int(hashlib.sha256((str(C1)+str(C2)).encode()).hexdigest(), 16):
        return False

    return True

# **************** Two Commitments Hide the Same Secret Proof  ****************


# ***************get inverse*****************

def egcd(a, b):
    if a == 0:
        return (b, 0, 1)
    else:
        g, y, x = egcd(b % a, a)

        return (g, x - (b // a) * y, y)


def modinv(a, m):  # find inverse
    g, x, y = egcd(a, m)
    if g != 1:
        raise Exception('modular inverse does not exist')
    else:
        return x % m

# ***************get inverse*****************


# ***********get getprime*************

def miller_rabin(n, k):
    if n == 2 or n % 2 == 0:
        return True
    if not n & 1:
        return False

    def check(a, s, d, n):
        x = pow(a, d, n)
        if x == 1:
            return True
        for i in range(s - 1):
            if x == n - 1:
                return True
            x = pow(x, 2, n)
        return x == n - 1

    s = 0
    d = n - 1
    count = 0

    while d % 2 == 0:
        d >>= 1
        s += 1

    for i in range(k):
        a = randrange(2, n - 1)
        if not check(a, s, d, n):
            count += 1

    return True if count == k else False


def getprime():
    # n = pow(2,512)+1
    # n =331172955124362089754565485563265540649
    # k =10

    while True:
        n = random.getrandbits(Spra)
        if miller_rabin(n, 20):
            continue
        else:
            return n

# ***********get getprime*************


def commit(m, r, g=par["g"], h=par["h"], N=par["N"]):
    if m < 0:
        G = modinv(pow(g, abs(m), N), N)
    else:
        G = pow(g, m, N)

    if r < 0:
        H = modinv(pow(h, abs(r), N), N)
    else:
        H = pow(h, r, N)

    c = (G * H) % N
    return c


def committer(m):
    r = random.getrandbits(SRpra)
    c = commit(m, r)

    return {
        "number": m,
        "confounding": hex(r),
        "commitment": hex(c),
    }

    # confounding = '{0:0{1}x}'.format(r, 320)
    # commitment = '{0:0{1}x}'.format(c, 128)

    # return {
    #     "number": m,
    #     "confounding": [
    #         '0x'+confounding[0:64],
    #         '0x'+confounding[64:128],
    #         '0x'+confounding[128:192],
    #         '0x'+confounding[192:256],
    #         '0x'+confounding[256:320],
    #     ],
    #     "commitment": [
    #         '0x'+commitment[0:64],
    #         '0x'+commitment[64:128],
    #     ],
    # }


def prover(a, b, m, r):
    g = par["g"]
    h = par["h"]
    N = par["N"]

 

    c = commit(m, r)

    print(hex(c))
    # while True:
    #    r = random.getrandbits(SRpra)
    #    c = commit(m, r, g, h, N)

    if a-1 < 0:
        c1 = (c * pow(g, abs(a-1), N)) % N
    else:
        c1 = (c * modinv(pow(g, a-1, N), N)) % N
    c2 = (pow(g, (b+1), N)*modinv(c, N)) % N

    if (c1 == 0 or c2 == 0 or c == 0):
        raise BaseException

    rP = random.getrandbits(SRpra)

    if b-m+1 < 0:
        cP = (modinv(pow(c1, abs(b-m+1), N), N) * pow(h, rP, N)) % N
    else:
        cP = (pow(c1, (b-m+1), N) * pow(h, rP, N)) % N

    el = EL(b-m+1, (-r), rP, g, h, c1, h, c2, cP, N)  # (H,X,X1,X2)

    w = random.getrandbits(SRpra)
    rPP = random.getrandbits(SRpra)
    cPP = (pow(cP, w**2, N) * pow(h, rPP, N)) % N

    sq1 = SQR(w, rPP, cP, h, cPP, N)  # (el,F,EP)

    while True:
        M = pow(random.getrandbits(SRpra), 2)
        if pow(w, 2) * (m-a+1) * (b-m+1) <= 0:
            R = pow(w, 2) * (m-a+1) * (b-m+1) - M
            break
        else:
            R = pow(w, 2) * (m-a+1) * (b-m+1) - M
            if (R > 0):
                break

    while True:
        r1 = random.getrandbits(SRpra)
        if (b-m+1) <= 0:
            z = pow(w, 2)*((b-m+1) * r + rP) + rPP - r1
            break
        else:
            z = pow(w, 2)*((b-m+1) * r + rP) + rPP - r1
            if (z > 0):
                break

    c1P = commit(M, r1, g, h, N)
    c2P = commit(1, z, 1, h, N)
    sq2 = SQR(M, r1, g, h, c1P, N)  # (el,F,EP)

    # return (0, cP, cPP, c1P, c2P, R, el, sq1, sq2)
    return {
        "c": 0,
        "cP": cP,
        "cPP": cPP,
        "c1P": c1P,
        "c2P": c2P,
        "R": R,
        "el": el,
        "sq1": sq1,
        "sq2": sq2
    }
    # return (c, c1, c2, cP, cPP, c1P, c2P, R, el, sq1, sq2)


# PI=(c_0,cP_1,cPP_2,c1P_3,c2P_4,R_5,el_6,sq1_7,sq2_8) par = system parameter
def verifier(a, b, commitment, PI):
    g = par["g"]
    h = par["h"]
    N = par["N"]

    if a-1 < 0:
        c1 = (commitment * pow(g, abs(a-1), N)) % N
        # if not PI[1] % N == (commitment * pow(g, abs(a-1), N))%N:
        # print "1_0"
        # return False
    else:
        c1 = commitment * modinv(pow(g, a-1, N), N)
        # if not PI[1] % N == commitment * modinv(pow(g, a-1, N),N):
        # print "1_1"
        # return False

    c2 = (pow(g, (b+1), N) * modinv(commitment, N)) % N

    # if not (PI[2] % N == (pow(g, (b+1), N) * modinv(commitment, N)) % N):
    # print "2"
    # return False

    # check EL
    if not VEL(PI[6], g, h, c1, h, c2, PI[1], N):
        # print("EL")
        return False

    if not (VSQ(PI[7], PI[1], h, PI[2], N)):
        # print("SQR1 w^2")
        return False

    GR = commit(1, PI[5], 1, g, N)

    if not (PI[2] % N == PI[3] * PI[4] * GR % N):
        # print("c''=c1'c2'")
        return False

    if not (VSQ(PI[8], g, h, PI[3], N)):
        # print("SQR2 M=a^2")
        return False

    if not (PI[5] > 0):
        # print("R>0")
        return False

    return True


def setup():
    cryptogen = SystemRandom()

    # a = 0 # 0 has some problem
    # b = math.pow(2,64)

    p = getprime()
    q = getprime()
    N = p * q

    # g = cryptogen.randrange(N)
    g = 2
    h = pow(g, cryptogen.randrange(N) >> 2, N)
    # return (N, g, h)
    # return (1, 100, 11, 2, 10)
    return {
        "N": N,
        "g": g,
        "h": h,
    }


if __name__ == "__main__":
    command = sys.argv[1]

    if command == "committer":
        number = int(sys.argv[2])
        print(json.dumps(committer(number)))
   

    elif command == "prover":
        lowerbound = int(sys.argv[2])
        upperboud = int(sys.argv[3])
        number = int(sys.argv[4])
        confounding = str(sys.argv[5])
        confounding = int(confounding,16)

        

        print(json.dumps(prover(lowerbound, upperboud, number, confounding)), end="")
    

    elif command == "verifier":
        commitment = int(sys.argv[2], 16)
        proof = eval(sys.argv[3])
        print(verifier(lowerbound, upperboud, commitment, proof), end="")
 


    elif command == "check":
        number = int(sys.argv[2])
        confounding = int(sys.argv[3], 16)
        commitment = int(sys.argv[4], 16)
        print(commit(number, confounding) == commitment, end="")
     

    elif command == "addCommitment":
        ca = int(sys.argv[2], 16)
        cb = int(sys.argv[3], 16)
        print("0x{0:0{1}x}".format((ca * cb % par["N"]), 128), end="")


    elif command == "addConfounding":
        ca = int(sys.argv[2], 16)
        cb = int(sys.argv[3], 16)
        print("0x{0:0{1}x}".format(ca + cb, 320), end="")
    
