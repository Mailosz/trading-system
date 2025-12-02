export function getStatusLabel(status: string): string {
    switch (status) {
        case "SUBMITTED":
            return "Oczekujące";
        case "FILLED":
            return "Wykonane";
        case "EXPIRED":
            return "Wygasłe";
        default:
            return "Nieznany status";
    }
}