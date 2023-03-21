const { createApp } = Vue;
createApp({
    data() {
        return {
            data: [],
            navbar: false,
            title: true,
            numAccountDestini: "",
            numAccountOrigin: "",
            description: "",
            amount: "",
            show: "own",
            accounts: [],
            avatarImg: 1,
        }
    },
    created() {
        this.avatarImg = localStorage.getItem("myAvatar") ? localStorage.getItem("myAvatar") : 1;
        this.loadData();
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
        logout: function () {
            Swal.fire({
                title: 'Are you sure to go out?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/logout')
                        .then(response => {
                            Swal.fire('Logout successful!', '', 'success')
                            window.location.href = '/web/index.html';
                        })
                        .catch(error => {
                            this.error = error.response.data;
                        });
                }
            })
        },
        newTransfer: function () {
            Swal.fire({
                title: 'Do you want to save the changes?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&numAccountOrigin=${this.numAccountOrigin}&numAccountDestini=${this.numAccountDestini}`)
                .then(response => {
                    Swal.fire('Successful Transfer')
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                    })
                })
                }
            })
        },
        filterAccounts: function () {
            this.accounts = this.data.accounts.filter(account => account.number != this.numAccountOrigin);
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