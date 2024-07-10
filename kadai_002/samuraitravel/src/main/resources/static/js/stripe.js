const stripe = Stripe('pk_test_51PU8Uo2K26S1A1y4WGkEuDOe1P6oTWhMwphKmrTF1kE9HrLpT3ogQfXkH0sOcLssomvqBQ0mN3siiYfjtLtHj6gN002Z8UMRnV');
const paymentButton =document.querySelector('#paymentButton');

paymentButton.addEventListener('click', ()=> {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
})