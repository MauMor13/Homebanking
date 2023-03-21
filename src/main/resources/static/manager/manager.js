const { createApp } = Vue;

createApp({
    data() {
        return {
            rest: undefined,
            clients: [],
            newClient: {
                firstName: "",
                lastName: "",
                email: "",
            },
            modiClient: {},
            name: "",
            maxAmount: "",
            percentage: "",
            payments: "",
            totalPayments: new Array([3, 6, 9, 12, 18], [12, 18, 24], [18, 42, 68], [3, 6, 9, 12, 18, 24])
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        addClient: function () {
            axios.post("/api/clients", this.newClient)
                .then(res => this.loadData())
                .catch(err => console.log(err));
        },
        loadData: function () {
            axios.get("/api/clients")
                .then(response => {
                    this.rest = response.data;
                    this.clients = response.data;
                    this.newClient.firstName = "";
                    this.newClient.lastName = "";
                    this.newClient.email = "";
                })
                .catch(err => console.log(err));
        },
        createNewLoan: function () {
            Swal.fire({
                title: 'Are you sure to go out?',
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/new-loan", `name=${this.name}&maxAmount=${this.maxAmount}&payment=${this.payments}&percentage=${this.percentage}`)
                        .then(response => {
                            Swal.fire('Create loan successful!', '', 'success')
                        })
                        .catch(error => {
                            this.error = error.response.data;
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                            })
                        });
                }
            })
        },
        deleteClient: function () {
            axios.delete(this.modiClient._links.self.href)
                .then(res => this.loadData())
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
        modify: function (client) {
            this.modiClient = { ...client };
        },
        modifyClient: function () {
            axios.put(this.modiClient._links.self.href, this.modiClient)
                .then(res => {
                    this.loadData();
                })
                .catch(err => console.log(err));
        }
    }
}).mount('#app');


