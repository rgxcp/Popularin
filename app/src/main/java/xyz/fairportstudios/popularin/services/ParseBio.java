package xyz.fairportstudios.popularin.services;

import xyz.fairportstudios.popularin.models.CreditDetail;

public class ParseBio {
    public String getBioForHumans(CreditDetail creditDetail) {
        String name = creditDetail.getName();
        String department = creditDetail.getKnown_for_department();
        String dob = creditDetail.getBirthday();
        String pob = creditDetail.getPlace_of_birth();
        String dateForHumans = new ParseDate().getDateForHumans(dob);

        if (dateForHumans.equals("Tanpa Tahun")) {
            dateForHumans = "yang belum diketahui";
        }
        if (pob.length() == 4) {
            pob = "tempat yang belum diketahui";
        }

        return name
                + " adalah seorang yang dikenal dalam bidang "
                + department.toLowerCase()
                + " yang lahir pada tanggal "
                + dateForHumans
                + " di "
                + pob
                + ".";
    }
}
