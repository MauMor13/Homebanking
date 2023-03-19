//app the vue
const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            cards: [],
            navbar: false,
            title: true,
            typeCard: "CREDIT",
            allCards: [],
        }
    },
    created() {
        this.loadData();
        this.loadCards();
    },
    methods: {
        loadData: function () {
            axios.get("/api/clients/current")
                .then(response => {
                    this.data = response.data;
                    this.data.accounts.sort((a, b) => a.id - b.id);
                })
                .catch(err => console.log(err));
        },
        loadCards: function () {
            axios.get("/api/clients/current/cards")
                .then(response => {
                    console.log(response.data);
                    this.allCards = response.data;
                    this.cardType()
                })
                .catch(err => console.log(err));
        },
        deleteCard: function (number){
            axios.patch("/api/card-delete",`numberCard=${number}`)
            .then(response => {
                this.loadCards();
                Swal.fire('Account Delete Successfully');
            })
            .catch(error => {
                console.log(error.data);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
                })
            });
        },
        logout: function () {
            axios.post('/api/logout')
                .then(response => {
                    window.location.href = '/web/index.html';
                })
                .catch(error => {
                    this.error = error.response.data.message;
                });
        },
        colorCard: function (color) {
            if (color == "SILVER")
                return "url(https://media.istockphoto.com/id/1051466618/es/vector/fondo-de-tecnolog%C3%ADa-con-textura-metal.jpg?s=612x612&w=0&k=20&c=TNQ6UyN2SJH8jj2BkQfTzUp0Kxh0GBiAHz1lAevhpVA=)";
            if (color == "GOLD")
                return "url(https://i.pinimg.com/originals/96/36/3c/96363c9337b2d1aad24323b1d9efda72.jpg)";
            else
                return "url(https://media.istockphoto.com/id/1320912181/vector/abstract-gray-gradient-metallic-texture.jpg?s=612x612&w=0&k=20&c=R04SBrq_5Li51HaGl3MCeObiLml0yRSeQfIiweRxmHQ=)";
        },
        cardType: function () {
            this.cards = this.allCards.filter(cards => cards.type.includes(this.typeCard));
        },
        navShow: function (value) {
            this.navbar = value;
        },
        beforeDestroy: function () {
            window.removeEventListener("resize", this.updateScreenSize);
        },
        updateScreenSize: function () {
            this.title = window.innerWidth > 500;
        },
    },
    mounted() {
        this.updateScreenSize();
        window.addEventListener("resize", this.updateScreenSize);
    },
}).mount('#app');