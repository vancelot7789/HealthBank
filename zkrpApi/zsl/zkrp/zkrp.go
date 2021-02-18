package zkrp

import (
	"crypto/rand"
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"math/big"
)

var (
	// Spra   random number length
	Spra = 256
	// SRpra  secure parameter
	SRpra = new(big.Int).Exp(big2, big.NewInt(1024), nil)

	// par contains N, g, h
	parN, _ = new(big.Int).SetString("1730319361222680428228290691072800465681558218004766326513192206944185807019297659343329143903818492158850581211231765286181019363240633008554131425173323", 10)
	parG    = big.NewInt(2)
	parH, _ = new(big.Int).SetString("1531199841189175461873773360868120469608134006525219501102095337818440502129328665593613507872551613209395420879786426238875863915627205422651985208980175", 10)
	par     = struct{ N, g, h *big.Int }{parN, parG, parH}
	big0    = big.NewInt(0)
	big1    = big.NewInt(1)
	big2    = big.NewInt(2)
)

// SQR ...
func SQR(a *big.Int, r1 *big.Int, g *big.Int, h *big.Int, E *big.Int, N *big.Int) []*big.Int {
	r2 := new(big.Int)
	r3 := new(big.Int)
	for {
		r2, _ := rand.Int(rand.Reader, SRpra)
		r3.Sub(r1, r2)
		if r3.Cmp(big0) > 0 { // r3 > 0
			break
		}
	}
	F := new(big.Int)
	EP := new(big.Int)
	F.Mod(big.NewInt(0).Mul(big.NewInt(0).Exp(g, a, N), big.NewInt(0).Exp(h, r2, N)), N)  // (pow(g, a, N)*pow(h, r2, N)) % N
	EP.Mod(big.NewInt(0).Mul(big.NewInt(0).Exp(F, a, N), big.NewInt(0).Exp(h, r3, N)), N) // (pow(F, a, N)*pow(h, r3, N)) % N
	el := EL(a, r2, r3, g, h, F, h, F, EP, N)
	return append(el, F, EP)
}

// VSQ ...
func VSQ(sq []*big.Int, g *big.Int, h *big.Int, E *big.Int, N *big.Int) bool {
	A1 := commit(sq[1], sq[2], g, h, N)
	A2 := commit(sq[1], sq[3], sq[4], h, N)

	C1 := big.NewInt(0).Mod(big.NewInt(0).Mul(A1, big.NewInt(0).ModInverse(big.NewInt(0).Exp(sq[4], sq[0], N), N)), N)
	C2 := big.NewInt(0).Mod(big.NewInt(0).Mul(A2, big.NewInt(0).ModInverse(big.NewInt(0).Exp(sq[5], sq[0], N), N)), N)

	s := sha256.Sum256([]byte(C1.String() + C2.String()))
	compareH := new(big.Int)
	compareH.SetBytes(s[:])

	if compareH.Cmp(sq[0]) != 0 {
		return false
	}

	return true
}

// *************** Committed Number is a Square Proof ***************

// **************** Two Commitments Hide the Same Secret Proof  ****************

// EL ...
func EL(m *big.Int, r1 *big.Int, r2 *big.Int, g1 *big.Int, h1 *big.Int, g2 *big.Int, h2 *big.Int, c1 *big.Int, c2 *big.Int, N *big.Int) []*big.Int {
	u, _ := rand.Int(rand.Reader, SRpra)
	v1, _ := rand.Int(rand.Reader, SRpra)
	v2, _ := rand.Int(rand.Reader, SRpra)

	C1 := commit(u, v1, g1, h1, N)
	C2 := commit(u, v2, g2, h2, N)

	s := sha256.Sum256([]byte(C1.String() + C2.String()))
	H := new(big.Int)
	H.SetBytes(s[:])

	X := big.NewInt(0).Add(u, big.NewInt(0).Mul(H, m))
	X1 := big.NewInt(0).Add(v1, big.NewInt(0).Mul(H, r1))
	X2 := big.NewInt(0).Add(v2, big.NewInt(0).Mul(H, r2))

	return []*big.Int{H, X, X1, X2}
}

// VEL ...
func VEL(el []*big.Int, g1 *big.Int, h1 *big.Int, g2 *big.Int, h2 *big.Int, c1 *big.Int, c2 *big.Int, N *big.Int) bool {

	A1 := commit(el[1], el[2], g1, h1, N)
	A2 := commit(el[1], el[3], g2, h2, N)

	C1 := big.NewInt(0).Mod(big.NewInt(0).Mul(A1, big.NewInt(0).ModInverse(big.NewInt(0).Exp(c1, el[0], N), N)), N)
	C2 := big.NewInt(0).Mod(big.NewInt(0).Mul(A2, big.NewInt(0).ModInverse(big.NewInt(0).Exp(c2, el[0], N), N)), N)

	s := sha256.Sum256([]byte(C1.String() + C2.String()))
	compareH := new(big.Int)
	compareH.SetBytes(s[:])

	if compareH.Cmp(el[0]) != 0 {
		return false
	}

	return true
}

// **************** Two Commitments Hide the Same Secret Proof  ****************

// ***********get getprime*************

func getprime() *big.Int {
	// n = pow(2,512)+1
	// n =331172955124362089754565485563265540649
	// k =10

	for {
		n, _ := rand.Prime(rand.Reader, Spra)
		isPrime := n.ProbablyPrime(20)
		if isPrime {
			return n
		}
	}
}

// ***********get getprime*************

// Committer ...
func Committer(m int) CommitmentPackage {
	r, _ := rand.Int(rand.Reader, SRpra)
	r, _ = new(big.Int).SetString("95427727585933369840806839297423430479503926338588470993036883647876920202272268553558660916220094180576159335435924881110200319572372777683692017060776019287244517869583607607020204977528522619846895942006480268416692809920804712681442140097506280955441350831207461222977469739755246230519032558420919730956", 10)
	//fmt.Println("0x"+r.Text(16))
	//fmt.Println(" ")
	c := commit(big.NewInt(int64(m)), r, par.g, par.h, par.N)

	return NewCommitmentPackage(m, "0x"+r.Text(16), "0x"+c.Text(16)) // number, confounding, commitment
}

func commit(m *big.Int, r *big.Int, g *big.Int, h *big.Int, N *big.Int) *big.Int {
	G := new(big.Int)
	H := new(big.Int)
	if m.Cmp(big0) < 0 { //  m < 0
		G.ModInverse(big.NewInt(0).Exp(g, big.NewInt(0).Abs(m), N), N)
	} else { //  m >= 0
		G.Exp(g, m, N)
	}

	if r.Cmp(big0) < 0 { //  r < 0
		H.ModInverse(big.NewInt(0).Exp(h, big.NewInt(0).Abs(r), N), N)
	} else { //  r >= 0
		H.Exp(h, r, N)
	}

	c := big.NewInt(0).Mod(big.NewInt(0).Mul(G, H), N)

	return c
}

// Prover ...
func Prover(m, lowerbound, upperbound int64, r string) string {
	newR := new(big.Int)
	newR.SetString(r, 16)
	proof := prover(big.NewInt(lowerbound), big.NewInt(upperbound), big.NewInt(m), newR)
	proofBytes, err := json.Marshal(proof)
	if err != nil {
		panic(err)
	}
	return string(proofBytes)
}

func prover(a *big.Int, b *big.Int, m *big.Int, r *big.Int) Proof {
	g := par.g
	h := par.h
	N := par.N

	c := commit(m, r, g, h, N)
	c1 := new(big.Int)
	// while true:
	//    r = random.getrandbits(SRpra)
	//    c = commit(m, r, g, h, N)

	if big.NewInt(0).Sub(a, big1).Cmp(big0) < 0 { //  a - 1 < 0
		c1.Mod(big.NewInt(0).Mul(c, big.NewInt(0).Exp(g, big.NewInt(0).Abs(big.NewInt(0).Sub(a, big1)), N)), N) // c1 = (c * pow(g, abs(a-1), N)) % N
	} else {
		c1.Mod(big.NewInt(0).Mul(c, big.NewInt(0).ModInverse(big.NewInt(0).Exp(g, big.NewInt(0).Sub(a, big1), N), N)), N) // c1 = (c * modinv(pow(g, a-1, N), N)) % N
	}
	c2 := big.NewInt(0).Mod(big.NewInt(0).Mul(big.NewInt(0).Exp(g, big.NewInt(0).Add(b, big1), N), big.NewInt(0).ModInverse(c, N)), N) // c2 = (pow(g, (b+1), N)*modinv(c, N)) % N

	if c1.Cmp(big0) == 0 || c2.Cmp(big0) == 0 || c.Cmp(big0) == 0 {
		fmt.Println("ERROR")
		return Proof{}
	}

	rP, _ := rand.Int(rand.Reader, SRpra)
	cP := new(big.Int)

	if big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1).Cmp(big0) < 0 { // b-m+1 < 0
		cP.Mod(big.NewInt(0).Mul(big.NewInt(0).ModInverse(big.NewInt(0).Exp(c1, big.NewInt(0).Abs(big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1)), N), N), big.NewInt(0).Exp(h, rP, N)), N)
	} else {
		cP.Mod(big.NewInt(0).Mul(big.NewInt(0).Exp(c1, big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1), N), big.NewInt(0).Exp(h, rP, N)), N)
	}

	// el = H,X,X1,X2
	el := EL(big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1), big.NewInt(0).Neg(r), rP, g, h, c1, h, c2, cP, N)

	w, _ := rand.Int(rand.Reader, SRpra)
	rPP, _ := rand.Int(rand.Reader, SRpra)
	cPP := big.NewInt(0).Mod(big.NewInt(0).Mul(big.NewInt(0).Exp(cP, big.NewInt(0).Exp(w, big2, big0), N), big.NewInt(0).Exp(h, rPP, N)), N)

	// sq1 = H, X, X1, X2, F, EP
	sq1 := SQR(w, rPP, cP, h, cPP, N) // (el,F,EP)

	M := new(big.Int)
	R := new(big.Int)
	for {
		localr, _ := rand.Int(rand.Reader, SRpra)
		M.Exp(localr, big2, big0)
		if big.NewInt(0).Mul(big.NewInt(0).Mul(big.NewInt(0).Exp(w, big2, big0), big.NewInt(0).Add(big.NewInt(0).Sub(m, a), big1)), big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1)).Cmp(big0) <= 0 {
			R.Sub(big.NewInt(0).Mul(big.NewInt(0).Mul(big.NewInt(0).Exp(w, big2, big0), big.NewInt(0).Add(big.NewInt(0).Sub(m, a), big1)), big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1)), M)
			break
		} else {
			R.Sub(big.NewInt(0).Mul(big.NewInt(0).Mul(big.NewInt(0).Exp(w, big2, big0), big.NewInt(0).Add(big.NewInt(0).Sub(m, a), big1)), big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1)), M)
			if R.Cmp(big0) > 0 { // R > 0
				break
			}
		}
	}

	r1 := new(big.Int)
	z := new(big.Int)
	for {
		r1, _ = rand.Int(rand.Reader, SRpra)
		if big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1).Cmp(big0) <= 0 {
			z.Add(big.NewInt(0).Mul(big.NewInt(0).Exp(w, big2, big0), big.NewInt(0).Add(big.NewInt(0).Mul(big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1), r), rP)), big.NewInt(0).Sub(rPP, r1))
			break
		} else {
			z.Add(big.NewInt(0).Mul(big.NewInt(0).Exp(w, big2, big0), big.NewInt(0).Add(big.NewInt(0).Mul(big.NewInt(0).Add(big.NewInt(0).Sub(b, m), big1), r), rP)), big.NewInt(0).Sub(rPP, r1))
			if z.Cmp(big0) > 0 {
				break
			}
		}
	}
	c1P := commit(M, r1, g, h, N)
	c2P := commit(big1, z, big1, h, N)

	// sq2 = H, X, X1, X2, F, EP
	sq2 := SQR(M, r1, g, h, c1P, N) // (el,F,EP)

	return NewProof(big.NewInt(0), cP, cPP, c1P, c2P, R, el, sq1, sq2)
	// return (c, c1, c2, cP, cPP, c1P, c2P, R, el, sq1, sq2)
}

// Verifier ...
func Verifier(commitment string, lowerbound, upperbound int64, proof Proof) bool {
	newCommitment := new(big.Int)
	newCommitment.SetString(commitment, 16)
	return verifier(big.NewInt(lowerbound), big.NewInt(upperbound), newCommitment, proof)
}

// Proof=(c_0,cP_1,cPP_2,c1P_3,c2P_4,R_5,el_6,sq1_7,sq2_8) par = system parameter
func verifier(a *big.Int, b *big.Int, commitment *big.Int, proof Proof) bool {
	g := par.g
	h := par.h
	N := par.N

	result := make(chan bool, 5)

	go func() {
		c1 := new(big.Int)
		if big.NewInt(0).Sub(a, big1).Cmp(big0) < 0 {
			c1.Mod(big.NewInt(0).Mul(commitment, big.NewInt(0).Exp(g, big.NewInt(0).Abs(big.NewInt(0).Sub(a, big1)), N)), N)
			// if not PI[1] % N == (commitment * pow(g, abs(a-1), N))%N:
			// print "1_0"
			// return false
		} else {
			c1.Mul(commitment, big.NewInt(0).ModInverse(big.NewInt(0).Exp(g, big.NewInt(0).Sub(a, big1), N), N))
			// if not PI[1] % N == commitment * modinv(pow(g, a-1, N),N):
			// print "1_1"
			// return false
		}
		c2 := big.NewInt(0).Mod(big.NewInt(0).Mul(big.NewInt(0).Exp(g, big.NewInt(0).Add(b, big1), N), big.NewInt(0).ModInverse(commitment, N)), N)

		// check EL
		if VEL(proof.EL, g, h, c1, h, c2, proof.CP, N) {
			result <- true
		} else {
			result <- false
		}
	}()
	go func() {
		if VSQ(proof.SQ1, proof.CP, h, proof.CPP, N) {
			result <- true
		} else {
			result <- false
		}
	}()
	go func() {
		GR := commit(big1, proof.R, big1, g, N)
		if big.NewInt(0).Mod(proof.CPP, N).Cmp(big.NewInt(0).Mod(big.NewInt(0).Mul(big.NewInt(0).Mul(proof.C1P, proof.C2P), GR), N)) == 0 {
			result <- true
		} else {
			result <- false
		}
	}()
	go func() {
		if VSQ(proof.SQ2, g, h, proof.C1P, N) {
			result <- true
		} else {
			result <- false
		}
	}()
	go func() {
		if proof.R.Cmp(big0) > 0 {
			result <- true
		} else {
			result <- false
		}
	}()

	return <-result && <-result && <-result && <-result && <-result
}

// AddCommitment add commitment with mul and mod
func AddCommitment(commitmentA, commitmentB string) string {
	ca, _ := new(big.Int).SetString(commitmentA, 16)
	cb, _ := new(big.Int).SetString(commitmentB, 16)
	if ca.Cmp(big.NewInt(0)) == 0 {
		return commitmentB
	}
	return big.NewInt(0).Mod(big.NewInt(0).Mul(ca, cb), par.N).Text(16)
}

// AddConfounding add two confounding
func AddConfounding(confoundingA, confoundingB string) string {
	ca, _ := new(big.Int).SetString(confoundingA, 16)
	cb, _ := new(big.Int).SetString(confoundingB, 16)
	return big.NewInt(0).Add(ca, cb).Text(16)
}

// Check is commitmentPackage are correct
func Check(number int, confounding string, commitment string) bool {
	n := big.NewInt(int64(number))
	c, _ := new(big.Int).SetString(confounding, 16)
	com, _ := new(big.Int).SetString(commitment, 16)
	return commit(n, c, par.g, par.h, par.N).Cmp(com) == 0
}

// CheckAmountCommitment check is sun of two commitment plain number is zero
func CheckAmountCommitment(commitmentA, commitmentB string, totalConfounding string) bool {
	totalCommitment := AddCommitment(commitmentA, commitmentB)
	return Check(0, totalConfounding, totalCommitment)
}
